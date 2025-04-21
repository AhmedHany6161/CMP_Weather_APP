package org.weather.de.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import compose_weather_app.composeapp.generated.resources.Res
import compose_weather_app.composeapp.generated.resources.close
import compose_weather_app.composeapp.generated.resources.country
import compose_weather_app.composeapp.generated.resources.latitude
import compose_weather_app.composeapp.generated.resources.local_time
import compose_weather_app.composeapp.generated.resources.location_information
import compose_weather_app.composeapp.generated.resources.longitude
import compose_weather_app.composeapp.generated.resources.name
import compose_weather_app.composeapp.generated.resources.region
import compose_weather_app.composeapp.generated.resources.timezone
import org.jetbrains.compose.resources.stringResource
import org.weather.de.app.dataLayer.onlineWeather.Location

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