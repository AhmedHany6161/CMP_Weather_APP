package org.weather.de.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
internal fun CurrentWeatherScreen(
    viewModel: CurrentLocationWeatherViewModel = viewModel { CurrentLocationWeatherViewModel() }
) {
    val currentWeatherState by viewModel.currentWeather.collectAsState()

    CurrentWeatherContent(currentWeatherState, onRefresh = viewModel::refreshCurrentLocationWeather)
}

@Composable
private fun CurrentWeatherContent(
    state: CurrentWeatherState,
    onRefresh: () -> Unit
) {
    when (state) {
        is CurrentWeatherState.Error -> {
            ErrorScreen(errorMessage = state.message, onTryAgain = onRefresh)
        }

        is CurrentWeatherState.Loading -> {
            LoadingScreen()
        }

        is CurrentWeatherState.Success -> {
            SuccessScreen(response = state.data)
        }
    }
}