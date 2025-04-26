package org.weather.de.app.dataLayer.weatherLocation

/**
 * Represents geographical location data, including a name, latitude, and longitude.
 *
 * @property name The name or description of the location.
 * @property latitude The latitude coordinate of the location, in degrees.
 * @property longitude The longitude coordinate of the location, in degrees.
 */
data class LocationData(val name: String, val latitude: Double, val longitude: Double)
