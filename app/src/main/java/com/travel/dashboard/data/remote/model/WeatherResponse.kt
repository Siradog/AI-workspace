package com.travel.dashboard.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "current")
    val current: CurrentWeather
)

@JsonClass(generateAdapter = true)
data class CurrentWeather(
    @Json(name = "temperature_2m")
    val temperature: Double,
    @Json(name = "weather_code")
    val weatherCode: Int
)
