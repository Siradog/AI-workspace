package com.travel.dashboard.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travel.dashboard.data.local.dao.ScheduleDao
import com.travel.dashboard.data.local.dao.TripDao
import com.travel.dashboard.data.local.entity.Genre
import com.travel.dashboard.data.local.entity.Schedule
import com.travel.dashboard.data.local.entity.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class TripDetailUiState(
    val trip: Trip? = null,
    val schedules: List<Schedule> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class TripDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val tripDao: TripDao,
    private val scheduleDao: ScheduleDao
) : ViewModel() {

    private val tripId: Int = checkNotNull(savedStateHandle["tripId"])
    private val _tripState = MutableStateFlow<Trip?>(null)

    val uiState: StateFlow<TripDetailUiState> = scheduleDao.getSchedulesForTrip(tripId)
        .combine(_tripState) { schedules, trip ->
            TripDetailUiState(
                trip = trip,
                schedules = schedules,
                isLoading = trip == null
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TripDetailUiState()
        )

    init {
        loadTrip()
    }

    private fun loadTrip() {
        viewModelScope.launch {
            _tripState.value = tripDao.getTripById(tripId)
        }
    }

    fun addSchedule(
        time: LocalDateTime,
        place: String,
        genre: Genre,
        memo: String,
        transportDetails: String
    ) {
        viewModelScope.launch {
            val schedule = Schedule(
                tripId = tripId,
                time = time,
                place = place,
                genre = genre,
                memo = memo.ifBlank { null },
                transportDetails = transportDetails.ifBlank { null }
            )
            scheduleDao.insertSchedule(schedule)
        }
    }
}
