package com.example.weatherapp.data.mapper

import com.example.weatherapp.data.remote.dto.WeatherDto
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.model.WeatherCondition

fun WeatherDto.toDomain(): Weather {

    return Weather(
        cityName = cityName,
        temperatureCelsius = main.temp,
        feelsLikeCelsius = main.feelsLike,
        humidity = main.humidity,
        description = weather[0].description,
        iconCode = weather[0].icon,
        windSpeedMs = wind.speed,
        condition = WeatherCondition.fromIconCode(weather[0].icon)
    )
}