package org.weather.de.app.dataLayer.offlineWeather

import org.weather.de.app.dataLayer.onlineWeather.CurrentWeatherResponse
import org.weather.de.app.dataLayer.weatherLocation.LocationData

interface OfflineRepository {
    suspend fun saveWeatherData(data: CurrentWeatherResponse?)

    suspend fun getCurrentWeather(locationData: LocationData?): CurrentWeatherResponse?
}
