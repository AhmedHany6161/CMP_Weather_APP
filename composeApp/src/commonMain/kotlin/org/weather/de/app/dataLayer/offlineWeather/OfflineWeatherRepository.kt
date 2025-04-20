package org.weather.de.app.dataLayer.offlineWeather

import org.weather.de.app.dataLayer.onlineWeather.CurrentWeatherResponse
import org.weather.de.app.dataLayer.weatherLocation.LocationData


object OfflineWeatherRepository : OfflineRepository {
    private val cacheMap by lazy { mutableMapOf<String, CurrentWeatherResponse?>() }

    override suspend fun saveWeatherData(data: CurrentWeatherResponse?) {
        cacheMap["${data?.location?.lat}-${data?.location?.lon}"] = data
    }

    override suspend fun getCurrentWeather(locationData: LocationData?): CurrentWeatherResponse? {
        return cacheMap["${locationData?.latitude}-${locationData?.longitude}"]
    }
}
