package org.weather.de.app.dataLayer.onlineWeather

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Query

/**
 * Interface defining the Ktorfit client for interacting with a weather API.
 *
 * This interface provides a function to retrieve current weather data based on a query string.
 * It uses Ktorfit annotations to define the API endpoint and request parameters.
 */
interface KtorFitClient {
    @Headers("Accept: application/json")
    @GET("current.json")
    suspend fun getWeatherData(
        @Query("q") query: String,
        @Query("key") key: String,
        @Query("aqi") aqi: String = "yes"
    ): CurrentWeatherResponse?
}