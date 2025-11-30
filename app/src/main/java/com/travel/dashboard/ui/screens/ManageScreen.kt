package com.travel.dashboard.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.travel.dashboard.data.local.entity.Todo
import com.travel.dashboard.data.local.entity.Trip
import com.travel.dashboard.ui.viewmodel.ManageViewModel

@Composable
fun ManageScreen(
    viewModel: ManageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Manage Todos",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.trips.isEmpty() && !uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Please create a trip in Dashboard first.")
                }
            } else {
                // Trip Selector (Top)
                TripSelector(
                    trips = uiState.trips,
                    selectedTrip = uiState.selectedTrip,
                    onTripSelected = { viewModel.selectTrip(it.id) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Todo List (Center - takes remaining space)
                TodoList(
                    todos = uiState.todos,
                    onToggle = { viewModel.toggleTodo(it) },
                    onDelete = { viewModel.deleteTodo(it) },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Todo Input (Bottom)
                TodoInput(
                    onAddTodo = { viewModel.addTodo(it) }
                )
            }
        }
    }
}

@Composable
fun TripSelector(
    trips: List<Trip>,
    selectedTrip: Trip?,
    onTripSelected: (Trip) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedTrip?.title ?: "Select Trip",
            onValueChange = {},
            readOnly = true,
            label = { Text("Selected Trip") },
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown, "Select Trip")
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )
        // Overlay generic clickable on top of TextField to handle click event
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            trips.forEach { trip ->
                DropdownMenuItem(
                    text = { Text(trip.title) },
                    onClick = {
                        onTripSelected(trip)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun TodoInput(
    onAddTodo: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("New Task") },
            modifier = Modifier.weight(1f),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    onAddTodo(text)
                    text = ""
                }
            }
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }
    }
}

@Composable
fun TodoList(
    todos: List<Todo>,
    onToggle: (Todo) -> Unit,
    onDelete: (Todo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(todos, key = { it.id }) { todo ->
            TodoItem(
                todo = todo,
                onToggle = onToggle,
                onDelete = onDelete
            )
        }
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    onToggle: (Todo) -> Unit,
    onDelete: (Todo) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isDone,
                onCheckedChange = { onToggle(todo) }
            )
            Text(
                text = todo.content,
                modifier = Modifier.weight(1f),
                style = if (todo.isDone) {
                    MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.LineThrough)
                } else {
                    MaterialTheme.typography.bodyLarge
                }
            )
            IconButton(onClick = { onDelete(todo) }) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ManageScreen() {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Manage Screen (Todo)")
        }
    }
}
