package org.weather.de.app.ui

import org.weather.de.app.dataLayer.onlineWeather.CurrentWeatherResponse

sealed class CurrentWeatherState {
    data class Success(val data: CurrentWeatherResponse) : CurrentWeatherState()
    data class Error(val message: String) : CurrentWeatherState()
    data object Loading : CurrentWeatherState()
}
