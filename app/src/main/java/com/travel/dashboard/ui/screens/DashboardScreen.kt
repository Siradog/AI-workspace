package com.travel.dashboard.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.travel.dashboard.data.local.entity.Trip
import com.travel.dashboard.ui.viewmodel.DashboardViewModel
import com.travel.dashboard.ui.viewmodel.WeatherInfo
import java.time.format.DateTimeFormatter

@Composable
fun DashboardScreen(
    onTripClick: (Int) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Placeholder for future implementation
                println("FAB Clicked")
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Trip")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Weather Card
            WeatherCard(
                weather = uiState.weather,
                isLoading = uiState.isLoadingWeather,
                error = uiState.weatherError
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Upcoming Trips",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Trip List
            TripList(
                trips = uiState.trips,
                onTripClick = onTripClick
            )
        }
    }
}

@Composable
fun WeatherCard(
    weather: WeatherInfo?,
    isLoading: Boolean,
    error: String?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Current Weather (Tokyo)",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else if (error != null) {
                Text(text = "Error: $error", color = MaterialTheme.colorScheme.error)
            } else if (weather != null) {
                Text(
                    text = "${weather.temperature}°C",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = weather.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(text = "No data")
            }
        }
    }
}

@Composable
fun TripList(
    trips: List<Trip>,
    onTripClick: (Int) -> Unit
) {
    if (trips.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No upcoming trips. Add one!")
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(trips) { trip ->
                TripItem(trip = trip, onClick = { onTripClick(trip.id) })
            }
        }
    }
}

@Composable
fun TripItem(
    trip: Trip,
    onClick: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = trip.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = trip.destination,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${trip.startDate.format(dateFormatter)} - ${trip.endDate.format(dateFormatter)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DashboardScreen(
    onTripClick: (Int) -> Unit
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Dashboard Screen")
            // TODO: Display list of trips and handle clicks calling onTripClick
        }
    }
}
