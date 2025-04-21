package org.weather.de.app.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.compose.LocalPlatformContext
import coil3.request.crossfade
import org.weather.de.app.dataLayer.onlineWeather.CurrentWeatherResponse
import compose_weather_app.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.weather.de.app.ui.components.AirQualityDetails
import org.weather.de.app.ui.components.LocationInfoDialog
import org.weather.de.app.ui.components.WeatherDetailRow

@Composable
fun SuccessScreen(response: CurrentWeatherResponse) {
    // State to control the visibility of the Air Quality details
    var showLocationInfoDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .padding(top = 64.dp)
            .fillMaxWidth().animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Location Information
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp).align(Alignment.CenterHorizontally)
        ) {
            // Use a clickable icon to show more location info in a dialog
            Icon(
                Icons.Filled.LocationOn,
                contentDescription = stringResource(Res.string.location),
                tint = MaterialTheme.colorScheme.primary, // Use primary color for the icon
                modifier = Modifier.clickable { showLocationInfoDialog = true }
            )
            Text(
                text = "${response.location.name}, ${response.location.region}, ${response.location.country}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold, // Use semibold for the location
                color = MaterialTheme.colorScheme.onSurface // Use appropriate color from theme
            )
        }

        // Temperature
        Text(
            text = "${response.current.tempC}°C / ${response.current.tempF}°F",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Condition
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp), // Increased spacing
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            // Use Coil for asynchronous image loading with caching
            val request = ImageRequest.Builder(LocalPlatformContext.current)
                .data("https:" + response.current.condition.icon) // Ensure https
                .crossfade(true)
                .build()

            AsyncImage(
                model = request,
                contentDescription = response.current.condition.text,
                modifier = Modifier.size(48.dp), // Increased size of the icon
                contentScale = ContentScale.Crop,
            )

            Text(
                text = response.current.condition.text,
                style = MaterialTheme.typography.titleMedium, // Use titleMedium for condition text
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 20.dp),
            color = MaterialTheme.colorScheme.outlineVariant // Use outline variant for divider
        )

        // Additional Details
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
            WeatherDetailRow(
                label = stringResource(Res.string.last_updated),
                value = response.current.lastUpdated,
                color = MaterialTheme.colorScheme.onSurface
            )
            WeatherDetailRow(
                label = stringResource(Res.string.feels_like),
                value = "${response.current.feelslikeC}°C / ${response.current.feelslikeF}°F",
                color = MaterialTheme.colorScheme.onSurface
            )
            WeatherDetailRow(
                label = stringResource(Res.string.humidity),
                value = "${response.current.humidity}%",
                color = MaterialTheme.colorScheme.onSurface
            )
            WeatherDetailRow(
                label = stringResource(Res.string.wind),
                value = "${response.current.windKph} km/h (${response.current.windDir})",
                color = MaterialTheme.colorScheme.onSurface
            )
            WeatherDetailRow(
                label = stringResource(Res.string.pressure),
                value = "${response.current.pressureMb} mb",
                color = MaterialTheme.colorScheme.onSurface
            )
            WeatherDetailRow(
                label = stringResource(Res.string.precipitation),
                value = "${response.current.precipMm} mm",
                color = MaterialTheme.colorScheme.onSurface
            )
            WeatherDetailRow(
                label = stringResource(Res.string.uv_index),
                value = response.current.uv.toString(),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        AirQualityDetails(response)
    }
    // Show dialog on location icon click
    if (showLocationInfoDialog) {
        LocationInfoDialog(
            location = response.location,
            onDismiss = { showLocationInfoDialog = false }
        )
    }
}


