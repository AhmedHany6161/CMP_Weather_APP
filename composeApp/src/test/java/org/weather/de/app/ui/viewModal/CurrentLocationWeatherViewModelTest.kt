package org.weather.de.app.ui.viewModal

import io.mockk.coEvery
import io.mockk.clearAllMocks
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.weather.de.app.dataLayer.offlineWeather.OfflineRepository
import org.weather.de.app.dataLayer.onlineWeather.CurrentWeatherResponse
import org.weather.de.app.dataLayer.onlineWeather.OnlineRepository
import org.weather.de.app.dataLayer.weatherLocation.LocationData
import org.weather.de.app.dataLayer.weatherLocation.LocationServices

@OptIn(ExperimentalCoroutinesApi::class)
class CurrentLocationWeatherViewModelTest {

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `searchResults initial state`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher()
        val viewModel = CurrentLocationWeatherViewModel(mockk(), mockk(), mockk(), testDispatcher)
        val searchResultsFlow: StateFlow<List<LocationData>> = viewModel.searchResults
        val expected: List<LocationData> = emptyList()
        val actual = searchResultsFlow.first()

        assertEquals(expected, actual)
    }

    @Test
    fun `searchResults with valid query`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher()

        val locationServices = mockk<LocationServices>()

        val mockLocationDataList = listOf(
            LocationData("London, UK", 51.5074, 0.1278),
            LocationData("London, Canada", 42.9849, -81.2453)
        )

        coEvery { locationServices.getSuggestionCompilation(any()) } returns mockLocationDataList

        val onlineWeatherRepository = mockk<OnlineRepository>()
        val offlineWeatherRepository = mockk<OfflineRepository>()

        val viewModel = CurrentLocationWeatherViewModel(
            onlineWeatherRepository,
            offlineWeatherRepository,
            locationServices,
            testDispatcher
        )

        val validQuery = "London"
        viewModel.onSearchQueryChanged(validQuery)
        val searchResultsFlow: StateFlow<List<LocationData>> = viewModel.searchResults

        val actual = searchResultsFlow.first()

        assertEquals(mockLocationDataList, actual)
    }

    @Test
    fun `currentWeather initial state`() = runTest {
        val testDispatcher = StandardTestDispatcher()
        val locationServices = mockk<LocationServices>()
        val onlineWeatherRepository = mockk<OnlineRepository>()
        val offlineWeatherRepository = mockk<OfflineRepository>()
        coEvery { locationServices.getCurrentLocation() } returns mockk()
        coEvery {
            offlineWeatherRepository.getCurrentWeather(any())
        }

        val viewModel =
            CurrentLocationWeatherViewModel(
                onlineWeatherRepository,
                offlineWeatherRepository,
                locationServices,
                testDispatcher
            )

        val actualState = viewModel.currentWeather.first()

        assertEquals(CurrentWeatherState.Loading, actualState)
    }

    @Test
    fun `currentWeather success state`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher()
        val locationServices = mockk<LocationServices>()
        val onlineWeatherRepository = mockk<OnlineRepository>()
        val offlineWeatherRepository = mockk<OfflineRepository>()
        val weatherResponse = mockk<CurrentWeatherResponse>()

        coEvery { locationServices.getCurrentLocation() } returns mockk()

        coEvery {
            offlineWeatherRepository.getCurrentWeather(any())
        } returns weatherResponse

        coEvery {
            offlineWeatherRepository.saveWeatherData(any())
        } returns Unit

        coEvery {
            onlineWeatherRepository.getWeatherData(any())
        } returns weatherResponse

        val viewModel =
            CurrentLocationWeatherViewModel(
                onlineWeatherRepository,
                offlineWeatherRepository,
                locationServices,
                testDispatcher
            )

        val actualState = viewModel.currentWeather.first()

        assertEquals(CurrentWeatherState.Success(weatherResponse), actualState)
    }

    @Test
    fun `currentWeather error state when getCurrentLocation failed`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher()
        val locationServices = mockk<LocationServices>()
        val onlineWeatherRepository = mockk<OnlineRepository>()
        val offlineWeatherRepository = mockk<OfflineRepository>()
        val weatherResponse = mockk<CurrentWeatherResponse>()

        coEvery { locationServices.getCurrentLocation() } throws Exception("error")

        coEvery {
            offlineWeatherRepository.getCurrentWeather(any())
        } returns weatherResponse

        coEvery {
            offlineWeatherRepository.saveWeatherData(any())
        } returns Unit

        coEvery {
            onlineWeatherRepository.getWeatherData(any())
        } returns weatherResponse

        val viewModel =
            CurrentLocationWeatherViewModel(
                onlineWeatherRepository,
                offlineWeatherRepository,
                locationServices,
                testDispatcher
            )

        val actualState = viewModel.currentWeather.first()

        assertEquals(CurrentWeatherState.Error("error"), actualState)
    }

    @Test
    fun `currentWeather error state when getCurrentWeather failed in offline repo`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher()
        val locationServices = mockk<LocationServices>()
        val onlineWeatherRepository = mockk<OnlineRepository>()
        val offlineWeatherRepository = mockk<OfflineRepository>()
        val weatherResponse = mockk<CurrentWeatherResponse>()

        coEvery { locationServices.getCurrentLocation() } returns mockk()

        coEvery {
            offlineWeatherRepository.getCurrentWeather(any())
        } throws Exception("error")

        coEvery {
            offlineWeatherRepository.saveWeatherData(any())
        } returns Unit

        coEvery {
            onlineWeatherRepository.getWeatherData(any())
        } returns weatherResponse

        val viewModel =
            CurrentLocationWeatherViewModel(
                onlineWeatherRepository,
                offlineWeatherRepository,
                locationServices,
                testDispatcher
            )

        val actualState = viewModel.currentWeather.first()

        assertEquals(CurrentWeatherState.Error("error"), actualState)
    }

    @Test
    fun `currentWeather error state when saveWeatherData failed in offline repo`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher()
        val locationServices = mockk<LocationServices>()
        val onlineWeatherRepository = mockk<OnlineRepository>()
        val offlineWeatherRepository = mockk<OfflineRepository>()
        val weatherResponse = mockk<CurrentWeatherResponse>()

        coEvery { locationServices.getCurrentLocation() } returns mockk()

        coEvery {
            offlineWeatherRepository.getCurrentWeather(any())
        } returns weatherResponse

        coEvery {
            offlineWeatherRepository.saveWeatherData(any())
        } throws Exception("error")

        coEvery {
            onlineWeatherRepository.getWeatherData(any())
        } returns weatherResponse

        val viewModel =
            CurrentLocationWeatherViewModel(
                onlineWeatherRepository,
                offlineWeatherRepository,
                locationServices,
                testDispatcher
            )

        val actualState = viewModel.currentWeather.first()

        assertEquals(CurrentWeatherState.Error("error"), actualState)
    }

    @Test
    fun `currentWeather error state when getWeatherData failed in online repo`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher()
        val locationServices = mockk<LocationServices>()
        val onlineWeatherRepository = mockk<OnlineRepository>()
        val offlineWeatherRepository = mockk<OfflineRepository>()
        val weatherResponse = mockk<CurrentWeatherResponse>()

        coEvery { locationServices.getCurrentLocation() } returns mockk()

        coEvery {
            offlineWeatherRepository.getCurrentWeather(any())
        } returns weatherResponse

        coEvery {
            offlineWeatherRepository.saveWeatherData(any())
        } returns Unit

        coEvery {
            onlineWeatherRepository.getWeatherData(any())
        } throws Exception("error")

        val viewModel =
            CurrentLocationWeatherViewModel(
                onlineWeatherRepository,
                offlineWeatherRepository,
                locationServices,
                testDispatcher
            )

        val actualState = viewModel.currentWeather.first()

        assertEquals(CurrentWeatherState.Error("error"), actualState)
    }
}