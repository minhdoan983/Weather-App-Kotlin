package com.example.weatherapp.domain.model

data class Weather(
    val cityName: String,
    val temperatureCelsius: Double,
    val feelsLikeCelsius: Double,
    val humidity: Int,
    val description: String,
    val iconCode: String,
    val windSpeedMs: Double,
    val uvIndex: Double,
    val condition: WeatherCondition
)

enum class WeatherCondition {
    SUNNY, CLOUDY, RAINY, STORMY, SNOWY, FOGGY, UNKNOWN;

    companion object {
        fun fromIconCode(code: String): WeatherCondition = when {
            code.startsWith("01") -> SUNNY
            code.startsWith("02") || code.startsWith("03") || code.startsWith("04") -> CLOUDY
            code.startsWith("09") || code.startsWith("10") -> RAINY
            code.startsWith("11") -> STORMY
            code.startsWith("13") -> SNOWY
            code.startsWith("50") -> FOGGY
            else -> UNKNOWN
        }
    }
}