package org.weather.de.app.dataLayer.offlineWeather

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.weather.de.app.dataLayer.onlineWeather.CurrentWeatherResponse
import org.weather.de.app.dataLayer.weatherLocation.LocationData


/**
 * `OfflineWeatherRepository` is an in-memory implementation of the `OfflineRepository` interface.
 * It stores weather data in a cache (a mutable map) and provides methods to save and retrieve
 * weather data for specific locations. It uses a mutex to ensure thread-safety during
 * concurrent access to the cache.
 *
 * This repository is intended for offline use, where data is cached and can be accessed
 * without a network connection.
 */
object OfflineWeatherRepository : OfflineRepository {
    private val cacheMap: MutableMap<String, CurrentWeatherResponse?> = mutableMapOf()
    private val mutex = Mutex()

    /**
     * Saves the provided weather data into the cache.
     *
     * This function stores the `CurrentWeatherResponse` in an internal cache map,
     * keyed by the latitude and longitude of the location. The key is a string
     * formatted as "{latitude}-{longitude}".
     *
     * This method is designed to be thread-safe by using a [Mutex] to protect
     * concurrent access to the internal cache map. It ensures that only one
     * coroutine can modify the cache at a time, preventing data corruption.
     *
     * @param data The [CurrentWeatherResponse] to save. If `null`, it will store null data for the specified location.
     * The location used for key generation is taken from `data?.location`. If `data?.location` is also null, then it will be ignored.
     *
     *
     * @throws [Exception] if something goes wrong during the Mutex lock acquiring or data saving.
     *
     * @sample
     * ```
     * val cache = WeatherDataCache()
     * val location = Location(34.0522, -118.2437) // Example: Los Angeles
     * val weatherData = CurrentWeatherResponse(location, // ... other data)
     * cache.saveWeatherData(weatherData)
     * ```
     */
    override suspend fun saveWeatherData(data: CurrentWeatherResponse?) {
        mutex.withLock {
            cacheMap["${data?.location?.lat}-${data?.location?.lon}"] = data
        }
    }

    /**
     * Retrieves the current weather data for a given location.
     *
     * This function attempts to retrieve weather data from an in-memory cache. The cache is keyed by
     * a string representation of the location's latitude and longitude (e.g., "34.0522-118.2437").
     * If the data is not present in the cache for the specified location, it returns null.
     *
     * The function utilizes a mutex to ensure thread safety when accessing the cache. This is crucial
     * in a concurrent environment where multiple threads might try to read or write to the cache
     * simultaneously.
     *
     * @param locationData An optional [LocationData] object containing the latitude and longitude of the
     *                     location for which to retrieve weather data. If null, the function will return null.
     * @return A [CurrentWeatherResponse] object representing the current weather conditions for the
     *         specified location, or null if:
     *           - `locationData` is null.
     *           - No cached data exists for the specified location.
     *           - The cache is empty.
     *
     * @throws Exception if any exception happens in accessing cache.
     */
    override suspend fun getCurrentWeather(locationData: LocationData?): CurrentWeatherResponse? {
        return mutex.withLock {
            cacheMap["${locationData?.latitude}-${locationData?.longitude}"]
        }
    }
}
