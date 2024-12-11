package com.example.task10

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class WeatherViewModel : ViewModel() {

    private val _weatherState = MutableStateFlow<List<DailyWeatherData>>(emptyList())
    val weatherState: StateFlow<List<DailyWeatherData>> = _weatherState

    data class DailyWeatherData(val date: String, val maxTemp: Double, val minTemp: Double)

    fun fetchWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getWeatherForecast(latitude, longitude)
                val dailyWeather = response.daily
                _weatherState.value = dailyWeather.time.mapIndexed { index, date ->
                    DailyWeatherData(
                        date = date,
                        maxTemp = dailyWeather.temperature_2m_max[index],
                        minTemp = dailyWeather.temperature_2m_min[index]
                    )
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Ошибка загрузки прогноза", e)
            }
        }
    }
}
