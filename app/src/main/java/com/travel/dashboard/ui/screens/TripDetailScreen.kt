package com.travel.dashboard.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.travel.dashboard.data.local.entity.Genre
import com.travel.dashboard.data.local.entity.Schedule
import com.travel.dashboard.data.local.entity.Trip
import com.travel.dashboard.ui.viewmodel.TripDetailViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun TripDetailScreen(
    tripId: Int,
    viewModel: TripDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddScheduleDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddScheduleDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Schedule")
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.trip != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                TripHeader(trip = uiState.trip!!)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Schedule",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                ScheduleList(schedules = uiState.schedules)
            }
        } else {
             Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Trip not found")
            }
        }

        if (showAddScheduleDialog) {
            AddScheduleDialog(
                onDismiss = { showAddScheduleDialog = false },
                onSave = { time, place, genre, memo, details ->
                    viewModel.addSchedule(time, place, genre, memo, details)
                    showAddScheduleDialog = false
                }
            )
        }
    }
}

@Composable
fun TripHeader(trip: Trip) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = trip.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = trip.destination,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${trip.startDate.format(dateFormatter)} - ${trip.endDate.format(dateFormatter)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ScheduleList(schedules: List<Schedule>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(schedules) { schedule ->
            ScheduleItem(schedule = schedule)
        }
    }
}

@Composable
fun ScheduleItem(schedule: Schedule) {
    val timeFormatter = DateTimeFormatter.ofPattern("MM/dd HH:mm")

    val icon = when (schedule.genre) {
        Genre.TRANSPORT -> Icons.Filled.Place // Or a direction car icon if available
        Genre.SIGHTSEEING -> Icons.Filled.Star
        Genre.MEAL -> Icons.Filled.Info // Or restaurant icon
    }

    val containerColor = when (schedule.genre) {
        Genre.TRANSPORT -> MaterialTheme.colorScheme.secondaryContainer
        Genre.SIGHTSEEING -> MaterialTheme.colorScheme.tertiaryContainer
        Genre.MEAL -> MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = schedule.genre.name,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = schedule.time.format(timeFormatter),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = schedule.place,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (!schedule.memo.isNullOrBlank()) {
                    Text(
                        text = schedule.memo,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (!schedule.transportDetails.isNullOrBlank()) {
                    Text(
                        text = "Info: ${schedule.transportDetails}",
                        style = MaterialTheme.typography.bodySmall,
                         fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleDialog(
    onDismiss: () -> Unit,
    onSave: (LocalDateTime, String, Genre, String, String) -> Unit
) {
    var dateTimeStr by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf(Genre.SIGHTSEEING) }
    var memo by remember { mutableStateOf("") }
    var transportDetails by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Add Schedule",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = dateTimeStr,
                    onValueChange = { dateTimeStr = it },
                    label = { Text("Time (yyyy-MM-dd HH:mm)") },
                    placeholder = { Text("2024-01-01 12:00") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = place,
                    onValueChange = { place = it },
                    label = { Text("Place") },
                    singleLine = true
                )

                // Genre Dropdown
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedGenre.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Genre") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            Genre.values().forEach { genre ->
                                DropdownMenuItem(
                                    text = { Text(genre.name) },
                                    onClick = {
                                        selectedGenre = genre
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = memo,
                    onValueChange = { memo = it },
                    label = { Text("Memo") }
                )

                OutlinedTextField(
                    value = transportDetails,
                    onValueChange = { transportDetails = it },
                    label = { Text("Transport Details") }
                )

                if (error != null) {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        try {
                            if (dateTimeStr.isBlank() || place.isBlank()) {
                                error = "Time and Place are required"
                                return@Button
                            }
                            val time = LocalDateTime.parse(dateTimeStr, dateTimeFormatter)
                            onSave(time, place, selectedGenre, memo, transportDetails)
                        } catch (e: DateTimeParseException) {
                            error = "Invalid format. Use yyyy-MM-dd HH:mm"
                        }
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
