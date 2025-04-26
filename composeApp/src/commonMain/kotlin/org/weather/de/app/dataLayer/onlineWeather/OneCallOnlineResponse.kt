package org.weather.de.app.dataLayer.onlineWeather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the response from a current weather API endpoint.
 *
 * This data class encapsulates the current weather information, including the location
 * and the current weather conditions at that location.
 *
 * @property location Information about the location for which the weather data is provided.
 * @property current The current weather conditions at the specified location.
 */
@Serializable
data class CurrentWeatherResponse(
    val location: Location,
    val current: Current
)

/**
 * Represents a geographic location with detailed information.
 *
 * @property name The name of the location (e.g., city, town).
 * @property region The region or state the location belongs to.
 * @property country The country the location is in.
 * @property lat The latitude coordinate of the location.
 * @property lon The longitude coordinate of the location.
 * @property tzId The timezone ID of the location (e.g., "America/New_York").
 * @property localtimeEpoch The local time at the location as a Unix timestamp (seconds since epoch).
 * @property localtime The local date and time at the location in "yyyy-MM-dd HH:mm" format.
 */
@Serializable
data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    @SerialName("tz_id")
    val tzId: String,
    @SerialName("localtime_epoch")
    val localtimeEpoch: Int,
    val localtime: String
)

/**
 * Represents the current weather conditions.
 *
 * This data class holds various details about the present weather, including temperature, wind,
 * precipitation, visibility, and air quality. It's designed to be used with Kotlin Serialization
 * to parse JSON responses from a weather API.
 *
 * @property lastUpdatedEpoch The last update time in Unix epoch time.
 * @property lastUpdated The last update time in local time.
 * @property tempC The temperature in Celsius.
 * @property tempF The temperature in Fahrenheit.
 * @property isDay Whether it is daytime (1) or nighttime (0).
 * @property condition The current weather condition, represented by a [Condition] object.
 * @property windMph The wind speed in miles per hour.
 * @property windKph The wind speed in kilometers per hour.
 * @property windDegree The wind direction in degrees.
 * @property windDir The wind direction as a 16-point compass direction (e.g., "N", "SW").
 * @property pressureMb The pressure in millibars.
 * @property pressureIn The pressure in inches.
 * @property precipMm The precipitation amount in millimeters.
 * @property precipIn The precipitation amount in inches.
 * @property humidity The relative humidity as a percentage.
 * @property cloud The cloud cover as a percentage.
 * @property feelslikeC The "feels like" temperature in Celsius.
 * @property feelslikeF The "feels like" temperature in Fahrenheit.
 * @property windchillC The wind chill temperature in Celsius.
 * @property windchillF The wind chill temperature in Fahrenheit.
 * @property heatindexC The heat index temperature in Celsius.
 * @property heatindexF The heat index temperature in Fahrenheit.
 * @property dewpointC The dew point temperature in Celsius.
 * @property dewpointF The dew point temperature in Fahrenheit.
 * @property visKm The visibility in kilometers.
 * @property visMiles The visibility in miles.
 * @property uv The UV index.
 * @property gustMph The wind gust speed in miles per hour.
 * @property gustKph The wind gust speed in kilometers per hour.
 * @property airQuality The air quality information, represented by an [AirQuality] object.
 */
@Serializable
data class Current(
    @SerialName("last_updated_epoch")
    val lastUpdatedEpoch: Int,
    @SerialName("last_updated")
    val lastUpdated: String,
    @SerialName("temp_c")
    val tempC: Double,
    @SerialName("temp_f")
    val tempF: Double,
    @SerialName("is_day")
    val isDay: Int,
    val condition: Condition,
    @SerialName("wind_mph")
    val windMph: Double,
    @SerialName("wind_kph")
    val windKph: Double,
    @SerialName("wind_degree")
    val windDegree: Int,
    @SerialName("wind_dir")
    val windDir: String,
    @SerialName("pressure_mb")
    val pressureMb: Double,
    @SerialName("pressure_in")
    val pressureIn: Double,
    @SerialName("precip_mm")
    val precipMm: Double,
    @SerialName("precip_in")
    val precipIn: Double,
    val humidity: Int,
    val cloud: Int,
    @SerialName("feelslike_c")
    val feelslikeC: Double,
    @SerialName("feelslike_f")
    val feelslikeF: Double,
    @SerialName("windchill_c")
    val windchillC: Double,
    @SerialName("windchill_f")
    val windchillF: Double,
    @SerialName("heatindex_c")
    val heatindexC: Double,
    @SerialName("heatindex_f")
    val heatindexF: Double,
    @SerialName("dewpoint_c")
    val dewpointC: Double,
    @SerialName("dewpoint_f")
    val dewpointF: Double,
    @SerialName("vis_km")
    val visKm: Double,
    @SerialName("vis_miles")
    val visMiles: Double,
    val uv: Double,
    @SerialName("gust_mph")
    val gustMph: Double,
    @SerialName("gust_kph")
    val gustKph: Double,
    @SerialName("air_quality")
    val airQuality: AirQuality
)

/**
 * Represents a weather condition.
 *
 * This data class encapsulates information about a specific weather condition, including a textual description,
 * an icon URL representing the condition, and a numerical code associated with the condition.
 *
 * @property text A textual description of the weather condition (e.g., "Sunny", "Partly cloudy", "Rainy").
 * @property icon A URL to an icon image that visually represents the weather condition.
 * @property code A numerical code that uniquely identifies the specific weather condition. This code can be used for
 *                 internal lookups or to retrieve more detailed information about the condition from a data source.
 */
@Serializable
data class Condition(
    val text: String,
    val icon: String,
    val code: Int
)

/**
 * Represents air quality data, including concentrations of various pollutants and air quality indices.
 *
 * @property co Carbon monoxide (CO) concentration in parts per million (ppm).
 * @property no2 Nitrogen dioxide (NO2) concentration in parts per million (ppm).
 * @property o3 Ozone (O3) concentration in parts per million (ppm).
 * @property so2 Sulfur dioxide (SO2) concentration in parts per million (ppm).
 * @property pm25 Particulate matter with a diameter of 2.5 micrometers or less (PM2.5) concentration in micrograms per cubic meter (μg/m³).
 * @property pm10 Particulate matter with a diameter of 10 micrometers or less (PM10) concentration in micrograms per cubic meter (μg/m³).
 * @property usEpaIndex Air Quality Index (AQI) based on the United States Environmental Protection Agency (US EPA) standard.
 *                       Ranges from 0 to 500, with higher values indicating worse air quality.
 *                       0-50: Good, 51-100: Moderate, 101-150: Unhealthy for Sensitive Groups, 151-200: Unhealthy, 201-300: Very Unhealthy, 301-500: Hazardous
 * @property gbDefraIndex Air Quality Index (AQI) based on the United Kingdom's Department for Environment, Food & Rural Affairs (GB DEFRA) standard.
 *                        Ranges from 1 to 10, with higher values indicating worse air quality.
 *                        1-3: Low, 4-6: Moderate, 7-9: High, 10: Very High
 */
@Serializable
data class AirQuality(
    val co: Double,
    val no2: Double,
    val o3: Double,
    val so2: Double,
    @SerialName("pm2_5")
    val pm25: Double,
    val pm10: Double,
    @SerialName("us-epa-index")
    val usEpaIndex: Int,
    @SerialName("gb-defra-index")
    val gbDefraIndex: Int
)