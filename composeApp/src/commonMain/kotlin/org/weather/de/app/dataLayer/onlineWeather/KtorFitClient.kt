package org.weather.de.app.dataLayer.onlineWeather

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Query

interface KtorFitClient {
    @Headers("Accept: application/json")
    @GET("current.json")
    suspend fun getWeatherData(
        @Query("q") query: String,
        @Query("key") key: String,
        @Query("aqi") aqi: String = "yes"
    ): CurrentWeatherResponse?
}