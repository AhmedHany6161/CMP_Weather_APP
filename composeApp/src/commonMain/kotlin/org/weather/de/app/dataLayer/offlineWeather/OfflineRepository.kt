package org.weather.de.app.dataLayer.offlineWeather

import org.weather.de.app.dataLayer.onlineWeather.CurrentWeatherResponse
import org.weather.de.app.dataLayer.weatherLocation.LocationData

/**
 * Interface for managing weather data in offline storage.
 *
 * This interface defines the contract for interacting with a local data source,
 * likely a database or file system, to persist and retrieve weather information
 * when the network is unavailable.
 */
interface OfflineRepository {
    suspend fun saveWeatherData(data: CurrentWeatherResponse?)

    suspend fun getCurrentWeather(locationData: LocationData?): CurrentWeatherResponse?
}
