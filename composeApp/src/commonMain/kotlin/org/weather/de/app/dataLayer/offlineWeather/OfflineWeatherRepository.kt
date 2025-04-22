package org.weather.de.app.dataLayer.offlineWeather

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.weather.de.app.dataLayer.onlineWeather.CurrentWeatherResponse
import org.weather.de.app.dataLayer.weatherLocation.LocationData


object OfflineWeatherRepository : OfflineRepository {
    private val cacheMap: MutableMap<String, CurrentWeatherResponse?> = mutableMapOf()
    private val mutex = Mutex()

    override suspend fun saveWeatherData(data: CurrentWeatherResponse?) {
        mutex.withLock {
            cacheMap["${data?.location?.lat}-${data?.location?.lon}"] = data
        }
    }

    override suspend fun getCurrentWeather(locationData: LocationData?): CurrentWeatherResponse? {
        return mutex.withLock {
            cacheMap["${locationData?.latitude}-${locationData?.longitude}"]
        }
    }
}
