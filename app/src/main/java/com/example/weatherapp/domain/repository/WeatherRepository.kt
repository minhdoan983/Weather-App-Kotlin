package com.example.weatherapp.domain.repository

import com.example.weatherapp.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository{
    fun getWeatherByCityName(cityName : String) : Flow<Result<Weather>>

    fun getWeatherByLocation(lat : Double, lon : Double) : Flow<Result<Weather>>
}