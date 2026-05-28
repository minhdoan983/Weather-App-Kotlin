package com.example.weatherapp.ui.event

sealed class WeatherUiEvent {
    data class SearchCity(val cityName: String) : WeatherUiEvent()
    data object UseCurrentLocation : WeatherUiEvent()
    data object Retry : WeatherUiEvent()
    data object ClearSearch : WeatherUiEvent()
}