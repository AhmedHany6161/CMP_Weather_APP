package org.weather.de.app.dataLayer.weatherLocation

import dev.jordond.compass.autocomplete.Autocomplete
import dev.jordond.compass.autocomplete.mobile
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.cancellation.CancellationException

/**
 * `LocationServicesHandler` is a singleton object responsible for handling location-related services.
 * It provides functionalities such as retrieving the current device location and fetching location suggestions based on a search query.
 *
 * It implements the `LocationServices` interface, ensuring that it provides the necessary location-related operations.
 *
 * Thread Safety:
 * This class uses Mutex objects to ensure thread safety when accessing and modifying shared resources related to current location retrieval and suggestion generation.
 */
object LocationServicesHandler : LocationServices {

    private val geoLocator: Geolocator by lazy { Geolocator.mobile() }
    private val autocomplete by lazy { Autocomplete.mobile() }
    private val mutexForCurrentLocation = Mutex()
    private val mutexForSuggestions = Mutex()

    /**
     * Retrieves the device's current geographical location.
     *
     * This function attempts to obtain the device's current location using the `geoLocator` service.
     * It uses a mutex (`mutexForCurrentLocation`) to ensure thread safety and prevent concurrent
     * access to the location retrieval process.
     *
     * On successful retrieval, a [LocationData] object is returned containing the latitude and
     * longitude. If the location cannot be determined, a default [LocationData] object is returned
     * with latitude and longitude set to 0.0. The location name will always be "current".
     *
     * This function must be called within a coroutine.
     *
     * @return A [LocationData] object representing the current location. If the location cannot be
     *         determined, the latitude and longitude will be 0.0, and the name will be "current".
     * @throws CancellationException If the coroutine is cancelled while waiting for the location.
     */
    override suspend fun getCurrentLocation(): LocationData {
        mutexForCurrentLocation.withLock {
            val location = geoLocator.current().getOrNull()
            return LocationData(
                "current",
                location?.coordinates?.latitude ?: 0.0,
                location?.coordinates?.longitude ?: 0.0
            )
        }
    }

    /**
     * Retrieves a list of suggested locations based on a search query.
     *
     * This function queries an external autocomplete service to find location suggestions
     * matching the provided `searchQuery`. It uses a coroutine `Mutex` (`mutexForSuggestions`) to ensure
     * thread safety when accessing and potentially modifying shared data related to suggestions.
     *
     * The results from the autocomplete service are transformed into a list of `LocationData`
     * objects. Each `LocationData` contains a display name, latitude, and longitude.
     *
     * The display name for each location is determined based on the availability of specific
     * address components, following this priority:
     *
     * 1. **Street:** If a street name is available (not null or blank), it's used as the display name.
     * 2. **Administrative Area:** If a street name is not available, but an administrative area is,
     *    the administrative area is used.
     * 3. **Country:** If neither a street name nor an administrative area is available, the country name is used.
     * 4. **Search Query:** If none of the above are available, the original `searchQuery` itself is used as the display name.
     *
     * **Thread Safety:** The use of `mutexForSuggestions.withLock` (within a coroutine scope) guarantees that only one
     * coroutine can execute the code within the block at a time. This prevents race conditions and data corruption when
     * multiple coroutines attempt to access or modify the suggestion data concurrently.
     *
     * **Error Handling:**  If the underlying autocomplete service call fails (e.g., due to a network issue),
     * this function returns an empty list instead of throwing an exception.
     *
     * @param searchQuery The text query used to search for locations. Must not be null or blank.
     * @return A list of `LocationData` objects representing the suggested locations.
     *         Returns an empty list if the search returns no results or if an error occurs during the search.
     * @see LocationData
     * @see kotlinx.coroutines.sync.Mutex
     *
     * Example:
     * ```kotlin
     * // Assuming you're in a coroutine scope
     * launch {
     *     val locations = getSuggestionCompilation("Eiffel Tower")
     *     if (locations.isNotEmpty()) {
     *         println("Found ${locations.size} locations:")
     */
    override suspend fun getSuggestionCompilation(searchQuery: String): List<LocationData> {
        mutexForSuggestions.withLock {
            return autocomplete.search(searchQuery).getOrNull()?.map { place ->

                val name: String? = if (!place.street.isNullOrBlank()) {
                    place.street
                } else if (!place.administrativeArea.isNullOrBlank()) {
                    place.administrativeArea
                } else {
                    place.country
                }

                LocationData(
                    name ?: searchQuery,
                    place.coordinates.latitude,
                    place.coordinates.longitude
                )
            } ?: emptyList()
        }
    }
}