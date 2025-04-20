package org.weather.de.app.dataLayer.onlineWeather

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.weather.de.app.dataLayer.weatherLocation.LocationData

private const val API_KEY = ""

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

    override suspend fun getWeatherData(locationData: LocationData): CurrentWeatherResponse? {
      return apiClient.getWeatherData("${locationData.latitude},${locationData.longitude}", API_KEY)
    }

}
