package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetWeatherByCityNameUseCase @Inject constructor(private val repository: WeatherRepository) {
    operator fun invoke(cityName: String): Flow<Result<Weather>> {
        val cityNameFormat = cityName.trim()
        if (cityNameFormat.isEmpty()) {
            return flowOf(
                Result.failure(
                    IllegalStateException("City name is can not empty!")
                )
            )
        }

        return repository.getWeatherByCityName(cityNameFormat)
    }
}

class GetWeatherByLocationUseCase @Inject constructor(private val repository: WeatherRepository) {
    operator fun invoke(lat: Double, lon: Double): Flow<Result<Weather>> {
        return repository.getWeatherByLocation(lat, lon)
    }
}