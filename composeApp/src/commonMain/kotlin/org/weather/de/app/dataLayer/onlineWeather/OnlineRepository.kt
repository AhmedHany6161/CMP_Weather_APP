package org.weather.de.app.dataLayer.onlineWeather

import org.weather.de.app.dataLayer.weatherLocation.LocationData

interface OnlineRepository {
    suspend fun getWeatherData(locationData: LocationData): CurrentWeatherResponse?
}