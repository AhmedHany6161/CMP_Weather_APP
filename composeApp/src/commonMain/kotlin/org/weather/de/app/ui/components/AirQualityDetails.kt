package org.weather.de.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose_weather_app.composeapp.generated.resources.Res
import compose_weather_app.composeapp.generated.resources.air_quality
import compose_weather_app.composeapp.generated.resources.co
import compose_weather_app.composeapp.generated.resources.gb_defra_index
import compose_weather_app.composeapp.generated.resources.no2
import compose_weather_app.composeapp.generated.resources.o3
import compose_weather_app.composeapp.generated.resources.pm10
import compose_weather_app.composeapp.generated.resources.pm25
import compose_weather_app.composeapp.generated.resources.so2
import compose_weather_app.composeapp.generated.resources.toggle_air_quality_details
import compose_weather_app.composeapp.generated.resources.us_epa_index
import org.jetbrains.compose.resources.stringResource
import org.weather.de.app.dataLayer.onlineWeather.CurrentWeatherResponse

@Composable
fun AirQualityDetails(response: CurrentWeatherResponse) {
    var isAirQualityExpanded by rememberSaveable { mutableStateOf(false) }

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
                imageVector = if (isAirQualityExpanded) Icons.AutoMirrored.Filled.ArrowForward else Icons.Outlined.Info,
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