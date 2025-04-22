package org.weather.de.app.dataLayer.weatherLocation

import dev.jordond.compass.autocomplete.Autocomplete
import dev.jordond.compass.autocomplete.mobile
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object LocationServicesHandler : LocationServices {

    private val geoLocator: Geolocator by lazy { Geolocator.mobile() }
    private val autocomplete by lazy { Autocomplete.mobile() }
    private val mutexForCurrentLocation = Mutex()
    private val mutexForSuggestions = Mutex()

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