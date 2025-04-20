package org.weather.de.app

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.weather.de.app.theme.AppTheme
import org.weather.de.app.ui.CurrentWeatherScreen

@Composable
internal fun App() = AppTheme {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
       return@AppTheme CurrentWeatherScreen()
    }
}
