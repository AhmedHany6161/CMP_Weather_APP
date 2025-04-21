package org.weather.de.app.dataLayer.weatherLocation

import dev.jordond.compass.autocomplete.Autocomplete
import dev.jordond.compass.autocomplete.mobile
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.mobile

object LocationServicesHandler : LocationServices {

    private val geoLocator: Geolocator by lazy { Geolocator.mobile() }
    private val autocomplete by lazy { Autocomplete.mobile() }

    override suspend fun getCurrentLocation(): LocationData {
        val location = geoLocator.current().getOrNull()
        return LocationData(
            "current",
            location?.coordinates?.latitude ?: 0.0,
            location?.coordinates?.longitude ?: 0.0
        )
    }

    override suspend fun getSuggestionCompilation(searchQuery: String): List<LocationData> {
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