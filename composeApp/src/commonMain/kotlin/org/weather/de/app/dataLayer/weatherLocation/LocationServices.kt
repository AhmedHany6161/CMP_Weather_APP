package org.weather.de.app.dataLayer.weatherLocation

interface LocationServices {
   suspend fun getCurrentLocation(): LocationData
   suspend fun getSuggestionCompilation(searchQuery: String): List<LocationData>
}