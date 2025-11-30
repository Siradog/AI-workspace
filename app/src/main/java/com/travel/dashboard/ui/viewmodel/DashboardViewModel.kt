package com.travel.dashboard.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travel.dashboard.data.local.dao.TripDao
import com.travel.dashboard.data.local.entity.Trip
import com.travel.dashboard.data.remote.WeatherApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class WeatherInfo(
    val temperature: Double,
    val weatherCode: Int,
    val description: String
)

data class DashboardUiState(
    val trips: List<Trip> = emptyList(),
    val weather: WeatherInfo? = null,
    val isLoadingWeather: Boolean = false,
    val weatherError: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val tripDao: TripDao,
    private val weatherApi: WeatherApi
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)

    // Internal state wrapper for weather
    private sealed class WeatherState {
        object Loading : WeatherState()
        data class Success(val info: WeatherInfo) : WeatherState()
        data class Error(val message: String) : WeatherState()
    }

    val uiState: StateFlow<DashboardUiState> = combine(
        tripDao.getAllTrips(),
        _weatherState
    ) { trips, weatherState ->
        val weatherInfo = (weatherState as? WeatherState.Success)?.info
        val isLoading = weatherState is WeatherState.Loading
        val error = (weatherState as? WeatherState.Error)?.message

        DashboardUiState(
            trips = trips,
            weather = weatherInfo,
            isLoadingWeather = isLoading,
            weatherError = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(isLoadingWeather = true)
    )

    init {
        fetchWeather()
    }

    private fun fetchWeather() {
        viewModelScope.launch {
            try {
                // Tokyo Station coordinates
                val latitude = 35.68
                val longitude = 139.76

                val response = weatherApi.getCurrentWeather(latitude, longitude)
                val current = response.current

                val info = WeatherInfo(
                    temperature = current.temperature,
                    weatherCode = current.weatherCode,
                    description = getWeatherDescription(current.weatherCode)
                )
                _weatherState.value = WeatherState.Success(info)
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error(e.localizedMessage ?: "Failed to load weather")
            }
        }
    }

    private fun getWeatherDescription(code: Int): String {
        return when (code) {
            0 -> "Clear sky"
            1, 2, 3 -> "Partly cloudy"
            45, 48 -> "Fog"
            51, 53, 55 -> "Drizzle"
            61, 63, 65 -> "Rain"
            71, 73, 75 -> "Snow"
            95, 96, 99 -> "Thunderstorm"
            else -> "Unknown ($code)"
        }
    }

    fun addTrip(title: String, destination: String, startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            val trip = Trip(
                title = title,
                destination = destination,
                startDate = startDate,
                endDate = endDate
            )
            tripDao.insertTrip(trip)
        }
    }
}
