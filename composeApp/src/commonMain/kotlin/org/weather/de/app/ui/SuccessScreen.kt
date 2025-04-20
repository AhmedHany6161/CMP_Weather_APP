package org.weather.de.app.ui

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import coil3.compose.LocalPlatformContext
import coil3.request.crossfade
import org.weather.de.app.dataLayer.onlineWeather.CurrentWeatherResponse
import org.weather.de.app.dataLayer.onlineWeather.Location
import compose_weather_app.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun SuccessScreen(response: CurrentWeatherResponse) {
    // State to control the visibility of the Air Quality details
    var isAirQualityExpanded by rememberSaveable { mutableStateOf(false) }
    var showLocationInfoDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
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

        // Air Quality
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 20.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                stringResource(Res.string.air_quality),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            // Use an icon to expand/collapse the air quality details
            IconButton(onClick = { isAirQualityExpanded = !isAirQualityExpanded }) {
                Icon(
                    imageVector = if (isAirQualityExpanded) Icons.Filled.ArrowForward else Icons.Outlined.Info,
                    contentDescription = stringResource(Res.string.toggle_air_quality_details),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }

        // Use AnimVisibility to control the expansion
        if (isAirQualityExpanded) {
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                WeatherDetailRow(
                    label = stringResource(Res.string.co),
                    value = response.current.airQuality.co.toString(),
                    color = MaterialTheme.colorScheme.secondary
                )
                WeatherDetailRow(
                    label = stringResource(Res.string.no2),
                    value = response.current.airQuality.no2.toString(),
                    color = MaterialTheme.colorScheme.secondary
                )
                WeatherDetailRow(
                    label = stringResource(Res.string.o3),
                    value = response.current.airQuality.o3.toString(),
                    color = MaterialTheme.colorScheme.secondary
                )
                WeatherDetailRow(
                    label = stringResource(Res.string.so2),
                    value = response.current.airQuality.so2.toString(),
                    color = MaterialTheme.colorScheme.secondary
                )
                WeatherDetailRow(
                    label = stringResource(Res.string.pm25),
                    value = response.current.airQuality.pm25.toString(),
                    color = MaterialTheme.colorScheme.secondary
                )
                WeatherDetailRow(
                    label = stringResource(Res.string.pm10),
                    value = response.current.airQuality.pm10.toString(),
                    color = MaterialTheme.colorScheme.secondary
                )
                WeatherDetailRow(
                    label = stringResource(Res.string.us_epa_index),
                    value = response.current.airQuality.usEpaIndex.toString(),
                    color = MaterialTheme.colorScheme.secondary
                )
                WeatherDetailRow(
                    label = stringResource(Res.string.gb_defra_index),
                    value = response.current.airQuality.gbDefraIndex.toString(),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
    // Show dialog on location icon click
    if (showLocationInfoDialog) {
        LocationInfoDialog(
            location = response.location,
            onDismiss = { showLocationInfoDialog = false }
        )
    }
}

@Composable
fun WeatherDetailRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray)
        Text(text = value, fontWeight = FontWeight.Medium, color = color)
    }
}

@Composable
fun LocationInfoDialog(location: Location, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.location_information),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "${stringResource(Res.string.name)}: ${location.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${stringResource(Res.string.region)}: ${location.region}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${stringResource(Res.string.country)}: ${location.country}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${stringResource(Res.string.latitude)}: ${location.lat}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${stringResource(Res.string.longitude)}: ${location.lon}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${stringResource(Res.string.timezone)}: ${location.tzId}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${stringResource(Res.string.local_time)}: ${location.localtime}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.close))
            }
        }
    )
}


