package org.weather.de.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.weather.de.app.dataLayer.weatherLocation.LocationServices
import org.weather.de.app.dataLayer.weatherLocation.LocationServicesHandler
import org.weather.de.app.dataLayer.offlineWeather.OfflineRepository
import org.weather.de.app.dataLayer.offlineWeather.OfflineWeatherRepository
import org.weather.de.app.dataLayer.onlineWeather.CurrentWeatherResponse
import org.weather.de.app.dataLayer.onlineWeather.OnlineRepository
import org.weather.de.app.dataLayer.onlineWeather.OnlineWeatherRepository
import org.weather.de.app.dataLayer.weatherLocation.LocationData

class CurrentLocationWeatherViewModel(
    private val onlineWeatherRepository: OnlineRepository = OnlineWeatherRepository,
    private val offlineWeatherRepository: OfflineRepository = OfflineWeatherRepository,
    private val locationServices: LocationServices = LocationServicesHandler,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private var job: Job? = null
    private val _currentWeather: MutableStateFlow<CurrentWeatherState> = MutableStateFlow(
        CurrentWeatherState.Loading
    )

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _currentWeather.value = CurrentWeatherState.Error(exception.message ?: "Unknown error")
    }

    val currentWeather: StateFlow<CurrentWeatherState> = _currentWeather

    init {
        job = viewModelScope.launch(dispatcher + exceptionHandler) {
            _currentWeather.value = CurrentWeatherState.Loading
            val currentLocation = locationServices.getCurrentLocation()
            val offlineCurrentWeather = offlineWeatherRepository.getCurrentWeather(currentLocation)
            if (offlineCurrentWeather != null) {
                _currentWeather.value = CurrentWeatherState.Success(offlineCurrentWeather)
            }
            fetchCurrentWeatherData(currentLocation)
        }
    }

    private suspend fun fetchCurrentWeatherData(currentLocation: LocationData) {
        val weatherData = onlineWeatherRepository.getWeatherData(currentLocation)
        weatherData?.let {
            _currentWeather.value = CurrentWeatherState.Success(it)
            offlineWeatherRepository.saveWeatherData(it)
        }
    }

    fun refreshCurrentLocationWeather() {
        job?.cancel()
        job = viewModelScope.launch(dispatcher + exceptionHandler) {
            _currentWeather.value = CurrentWeatherState.Loading
            val currentLocation = locationServices.getCurrentLocation()
            fetchCurrentWeatherData(currentLocation)
        }
    }
}