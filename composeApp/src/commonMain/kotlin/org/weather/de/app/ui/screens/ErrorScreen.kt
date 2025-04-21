package org.weather.de.app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import compose_weather_app.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorScreen(
    errorMessage: String,
    onTryAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animation state for subtle bounce effect
    val animatedProgress = remember { Animatable(0.95f) }

    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 1.05f,
            animationSpec = infiniteRepeatable(
                animation = tween(800),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Surface(
        modifier = modifier.fillMaxSize().padding(top = 56.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated error icon
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = stringResource(Res.string.animated_error_icon_description),
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .size(80.dp)
                    .scale(animatedProgress.value)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Error title with typography hierarchy
            Text(
                text = stringResource(Res.string.error_title),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Detailed error message
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Primary action button
                Button(
                    onClick = onTryAgain,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    modifier = Modifier.height(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = stringResource(Res.string.error_retry_icon_description),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(Res.string.error_try_again_title), style = MaterialTheme.typography.labelLarge)
                }

                // Secondary action button
                OutlinedButton(
                    onClick = { /* Handle secondary action */ },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(50.dp)
                ) {
                    Text(stringResource(Res.string.error_secondary_action_button_text), style = MaterialTheme.typography.labelLarge)
                }
            }

            // Additional support information
            Text(
                text = stringResource(Res.string.error_additional_support_information),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .padding(top = 32.dp)
                    .clickable { /* Handle support click */ }
            )
        }
    }
}