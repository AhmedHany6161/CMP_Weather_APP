package org.weather.de.app.ui.viewModal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.weather.de.app.dataLayer.weatherLocation.LocationServices
import org.weather.de.app.dataLayer.weatherLocation.LocationServicesHandler
import org.weather.de.app.dataLayer.offlineWeather.OfflineRepository
import org.weather.de.app.dataLayer.offlineWeather.OfflineWeatherRepository
import org.weather.de.app.dataLayer.onlineWeather.OnlineRepository
import org.weather.de.app.dataLayer.onlineWeather.OnlineWeatherRepository
import org.weather.de.app.dataLayer.weatherLocation.LocationData

class CurrentLocationWeatherViewModel(
    private val onlineWeatherRepository: OnlineRepository = OnlineWeatherRepository,
    private val offlineWeatherRepository: OfflineRepository = OfflineWeatherRepository,
    private val locationServices: LocationServices = LocationServicesHandler,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _currentWeather: MutableStateFlow<CurrentWeatherState> = MutableStateFlow(
        CurrentWeatherState.Loading
    )
    private val _selectedLocation: MutableStateFlow<LocationData?> = MutableStateFlow(null)

    private val _searchResults: MutableStateFlow<List<LocationData>> = MutableStateFlow(
        emptyList()
    )

    val searchResults: StateFlow<List<LocationData>> = _searchResults

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _currentWeather.value = CurrentWeatherState.Error(exception.message ?: "Unknown error")
    }

    val currentWeather: StateFlow<CurrentWeatherState> = _currentWeather

    init {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            val currentLocation = locationServices.getCurrentLocation()
            _selectedLocation.value = currentLocation
        }

        viewModelScope.launch(dispatcher + exceptionHandler) {
            _selectedLocation.collect { location ->
                location?.let {
                    _currentWeather.value = CurrentWeatherState.Loading
                    val offlineCurrentWeather = offlineWeatherRepository.getCurrentWeather(it)
                    offlineCurrentWeather?.let {
                        _currentWeather.value = CurrentWeatherState.Success(offlineCurrentWeather)
                    }
                    fetchWeatherData(it)
                }
            }
        }
    }


    private suspend fun fetchWeatherData(currentLocation: LocationData) {
        val weatherData = onlineWeatherRepository.getWeatherData(currentLocation)
        weatherData?.let {
            _currentWeather.value = CurrentWeatherState.Success(it)
            offlineWeatherRepository.saveWeatherData(it)
        }
    }

    fun onSearchQueryChanged(query: String) {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            val suggestions = locationServices.getSuggestionCompilation(query)
            _searchResults.value = suggestions
        }
    }

    fun onLocationSelected(location: LocationData) {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            _selectedLocation.value = location
        }
    }


    fun refreshCurrentLocationWeather() {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            _currentWeather.value = CurrentWeatherState.Loading
            _selectedLocation.value?.let {
                fetchWeatherData(it)
            }
        }
    }
}