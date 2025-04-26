package org.weather.de.app.ui.viewModal

import org.weather.de.app.dataLayer.onlineWeather.CurrentWeatherResponse

/**
 * A sealed class representing the different states that the current weather data can be in.
 *
 * This class provides a structured way to manage the state of asynchronous operations, specifically
 * the fetching and handling of current weather data. It defines three distinct states:
 * - [Success]: Indicates that the weather data has been successfully retrieved.
 * - [Error]: Indicates that an error occurred during the retrieval process.
 * - [Loading]: Indicates that the data is currently being fetched.
 */
sealed class CurrentWeatherState {
    /**
     * Represents the successful retrieval of current weather data.
     *
     * This data class encapsulates the [CurrentWeatherResponse] object that contains
     * the weather information obtained from the API. It signifies that the network
     * request to fetch the current weather was successful and the data is ready to be
     * displayed or processed.
     *
     * @property data The [CurrentWeatherResponse] object containing the details of the
     *                current weather.
     */
    data class Success(val data: CurrentWeatherResponse) : CurrentWeatherState()

    /**
     * Represents an error state in the current weather data retrieval process.
     *
     * This class encapsulates an error message that can be displayed to the user or logged for debugging purposes.
     * It is part of the [CurrentWeatherState] sealed class hierarchy, indicating that fetching the current weather
     * resulted in an error.
     *
     * @property message A string describing the error that occurred. This message should be user-friendly or
     *                   informative for developers.
     */
    data class Error(val message: String) : CurrentWeatherState()

    /**
     * Represents the loading state of the current weather data.
     *
     * This state indicates that the application is currently fetching the weather information
     * and the data is not yet available.  It's typically used to display a loading indicator
     * or placeholder content to the user while the data is being retrieved.
     *
     * This is a concrete implementation of the [CurrentWeatherState] sealed class.
     */
    data object Loading : CurrentWeatherState() { }
}
