package com.example.weatherapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("coord") val coord: CoordDto,
    @SerializedName("weather") val weather: List<WeatherDescriptionDto>,
    @SerializedName("base") val base: String,
    @SerializedName("main") val main: MainDto,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("wind") val wind: WindDto,
    @SerializedName("rain") val rain: RainDto,
    @SerializedName("clouds") val clouds: CloudsDto,
    @SerializedName("dt") val dt: Int,
    @SerializedName("timezone") val timeZone: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val cityName: String,
    @SerializedName("cod") val cod: Int
)
