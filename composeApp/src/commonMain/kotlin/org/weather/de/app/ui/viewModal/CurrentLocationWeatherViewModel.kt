package org.weather.de.app.ui.viewModal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.weather.de.app.dataLayer.weatherLocation.LocationServices
import org.weather.de.app.dataLayer.weatherLocation.LocationServicesHandler
import org.weather.de.app.dataLayer.offlineWeather.OfflineRepository
import org.weather.de.app.dataLayer.offlineWeather.OfflineWeatherRepository
import org.weather.de.app.dataLayer.onlineWeather.OnlineRepository
import org.weather.de.app.dataLayer.onlineWeather.OnlineWeatherRepository
import org.weather.de.app.dataLayer.weatherLocation.LocationData


/**
 * ViewModel responsible for managing the current location's weather data.
 * It interacts with online/offline repositories, uses location services, and provides search.
 *                            the current location and location suggestions. Defaults to [LocationServicesHandler].
 * @property dispatcher The CoroutineDispatcher used for background tasks. Defaults to [Dispatchers.IO].
 */
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

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _currentWeather.value = CurrentWeatherState.Error(exception.message ?: "Unknown error")
    }

    val searchResults = _searchResults.asStateFlow()

    val currentWeather = _currentWeather.asStateFlow()

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

    /**
     * Updates the search results based on the provided search query.
     *
     * This function is responsible for initiating the process of retrieving location suggestions
     * based on the user's input. It utilizes the `viewModelScope` to perform the potentially
     * time-consuming operation of fetching suggestions in a non-blocking manner. Updates the search results based on the provided search query.
     *
     * @param query The search query string.
     *
     * **State Update:**
     * Updates the `_searchResults` MutableStateFlow with the list of
     * suggestions returned by `getSuggestionCompilation`. This allows the UI to
     * dynamically reflect the new search results.
     */
        fun onSearchQueryChanged(query: String) {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            val suggestions = locationServices.getSuggestionCompilation(query)
            _searchResults.value = suggestions
        }
    }

    /**
     * Handles the selection of a location.
     *
     * Called when a location is selected. Updates the `_selectedLocation` LiveData.
     *
     * @param location The `LocationData` object representing the selected location.
     *
     * **State Update:**
     * Updates the selected location with the new location
     * @see LocationData
     */
    fun onLocationSelected(location: LocationData) {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            _selectedLocation.value = location
        }
    }


    /**
     * Refreshes the weather data for the currently selected location.
     *
     * This function initiates a refresh of the weather information for the user's currently selected location.
     * **State Update:** Sets the `_currentWeather` StateFlow to `CurrentWeatherState.Loading`.
     * If a location is selected, it triggers `fetchWeatherData` to retrieve the latest weather.
     */
    fun refreshCurrentLocationWeather() {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            _selectedLocation.value?.let {
                fetchWeatherData(it)
            }
        }
    }
}