package com.example.weatherapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RainDto(
    @SerializedName("rain") val rain : Double
)
