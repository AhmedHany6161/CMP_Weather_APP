package org.weather.de.app.dataLayer.onlineWeather

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.weather.de.app.dataLayer.weatherLocation.LocationData

private const val API_KEY = ""

/**
 * An object that provides an implementation of [OnlineRepository] for fetching weather data
 * from an online weather API (weatherapi.com).
 *
 * This object uses Ktorfit and Ktor HTTP Client to make network requests. It handles
 * JSON serialization and deserialization, and provides a simple interface to retrieve
 * current weather data for a given location.
 */
object OnlineWeatherRepository : OnlineRepository {
    private val ktorfit: Ktorfit by lazy {
        val ktorClient = HttpClient() {
            install(ContentNegotiation) {
                json(Json { isLenient = true; ignoreUnknownKeys = true })
            }
        }
        Ktorfit.Builder().baseUrl("https://api.weatherapi.com/v1/").httpClient(ktorClient).build()
    }

    private val apiClient: KtorFitClient by lazy {
        ktorfit.createKtorFitClient()
    }

    /**
     * Retrieves current weather data for a given location.
     *
     * This function uses the [KtorFitClient] to fetch weather information from a remote API.
     * It constructs the API request using the provided latitude and longitude from the
     * [LocationData] object.
     *
     * @param locationData The [LocationData] object containing the latitude and longitude of the
     *                     desired location. Must not be null.
     * @return A [CurrentWeatherResponse] object containing the current weather data if the request
     *         was successful. Returns `null` if the API request failed or returned an error.
     *
     * @throws Exception If any other error occurs during the process of getting weather data.
     *
     * @see CurrentWeatherResponse
     * @see LocationData
     */
    override suspend fun getWeatherData(locationData: LocationData): CurrentWeatherResponse? {
      return apiClient.getWeatherData("${locationData.latitude},${locationData.longitude}", API_KEY)
    }

}
