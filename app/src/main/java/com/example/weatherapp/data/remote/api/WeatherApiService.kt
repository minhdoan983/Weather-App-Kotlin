package com.example.weatherapp.data.remote.api

import com.example.weatherapp.data.remote.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "vi"
    ): WeatherDto

    @GET("data/2.5/weather")
    suspend fun getWeatherByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "vi"
    ): WeatherDto
}