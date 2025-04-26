package org.weather.de.app.dataLayer.onlineWeather

import org.weather.de.app.dataLayer.weatherLocation.LocationData

/**
 * Interface for fetching weather data from an online source.
 *
 * This interface defines the contract for interacting with a remote
 * weather data provider. It allows clients to retrieve current weather
 * information for a specific location.
 */
interface OnlineRepository {
    suspend fun getWeatherData(locationData: LocationData): CurrentWeatherResponse?
}