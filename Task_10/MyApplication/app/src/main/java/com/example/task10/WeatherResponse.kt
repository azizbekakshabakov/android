package com.example.task10

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val daily: DailyWeather
)