package org.weather.de.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.weather.de.app.dataLayer.weatherLocation.LocationData
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import org.weather.de.app.ui.viewModal.CurrentLocationWeatherViewModel
import org.weather.de.app.ui.viewModal.CurrentWeatherState

@Composable
internal fun CurrentWeatherScreen(
    viewModel: CurrentLocationWeatherViewModel = viewModel { CurrentLocationWeatherViewModel() }
) {
    val currentWeatherState by viewModel.currentWeather.collectAsState()
    val searchResults = viewModel.searchResults.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        CurrentWeatherContent(
            state = currentWeatherState,
            onRefresh = viewModel::refreshCurrentLocationWeather
        )

        WeatherSearchBar(
            textFieldState = remember { TextFieldState() },
            onQueryChange = { viewModel.onSearchQueryChanged(it) },
            searchResults = searchResults,
            onSelectItem = { viewModel.onLocationSelected(it) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSearchBar(
    textFieldState: TextFieldState,
    onQueryChange: (String) -> Unit,
    searchResults: State<List<LocationData>>,
    onSelectItem: (LocationData) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    DockedSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .semantics {
                isTraversalGroup = true
                traversalIndex = -1f
            },
        inputField = {
            SearchBarDefaults.InputField(
                query = textFieldState.text.toString(),
                onQueryChange = {
                    onQueryChange(it)
                    textFieldState.edit { replace(0, length, it) }
                },
                onSearch = { expanded = false },
                leadingIcon = {
                    if (expanded) {
                        IconButton(onClick = {
                            textFieldState.clearText()
                            expanded = false
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Search") }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .heightIn(max = 200.dp)
        ) {
            searchResults.value.forEach { result ->
                ListItem(
                    headlineContent = { Text(result.name) },
                    modifier = Modifier
                        .clickable {
                            onSelectItem(result)
                            textFieldState.edit { replace(0, length, result.name) }
                            expanded = false
                        }
                        .fillMaxWidth()
                )
            }
        }
    }
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