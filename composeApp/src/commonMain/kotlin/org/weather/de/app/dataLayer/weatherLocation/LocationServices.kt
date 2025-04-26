package org.weather.de.app.dataLayer.weatherLocation

/**
 * Interface for providing location-related services.
 * This interface defines methods for retrieving the current device location
 * and getting location suggestions based on a search query.
 */
interface LocationServices {
   suspend fun getCurrentLocation(): LocationData
   suspend fun getSuggestionCompilation(searchQuery: String): List<LocationData>
}