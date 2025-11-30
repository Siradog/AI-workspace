package com.travel.dashboard.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travel.dashboard.data.local.dao.TodoDao
import com.travel.dashboard.data.local.dao.TripDao
import com.travel.dashboard.data.local.entity.Todo
import com.travel.dashboard.data.local.entity.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ManageUiState(
    val trips: List<Trip> = emptyList(),
    val selectedTrip: Trip? = null,
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class ManageViewModel @Inject constructor(
    private val tripDao: TripDao,
    private val todoDao: TodoDao
) : ViewModel() {

    private val _selectedTripId = MutableStateFlow<Int?>(null)

    val uiState: StateFlow<ManageUiState> = combine(
        tripDao.getAllTrips(),
        _selectedTripId
    ) { trips, selectedId ->
        val selectedTrip = if (selectedId != null) {
            trips.find { it.id == selectedId }
        } else {
            trips.firstOrNull() // Default to first trip
        }

        // Side effect: update selected ID if it was null or invalid
        if (_selectedTripId.value != selectedTrip?.id) {
             _selectedTripId.value = selectedTrip?.id
        }

        Pair(trips, selectedTrip)
    }.flatMapLatest { (trips, selectedTrip) ->
        if (selectedTrip != null) {
            todoDao.getTodosForTrip(selectedTrip.id).map { todos ->
                ManageUiState(
                    trips = trips,
                    selectedTrip = selectedTrip,
                    todos = todos,
                    isLoading = false
                )
            }
        } else {
            flowOf(
                ManageUiState(
                    trips = trips,
                    selectedTrip = null,
                    todos = emptyList(),
                    isLoading = false
                )
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ManageUiState(isLoading = true)
    )

    fun selectTrip(tripId: Int) {
        _selectedTripId.value = tripId
    }

    fun addTodo(content: String) {
        val currentTripId = _selectedTripId.value ?: return
        if (content.isBlank()) return

        viewModelScope.launch {
            val todo = Todo(
                tripId = currentTripId,
                content = content
            )
            todoDao.insertTodo(todo)
        }
    }

    fun toggleTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.updateTodo(todo.copy(isDone = !todo.isDone))
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.deleteTodo(todo)
        }
    }
}
