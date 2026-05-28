package com.example.weatherapp.ui.state

import com.example.weatherapp.domain.model.Weather

sealed class WeatherUiState {

    /** Màn hình vừa mở, chưa search gì */
    data object Idle : WeatherUiState()

    /** Đang gọi API */
    data object Loading : WeatherUiState()

    /** API trả về thành công */
    data class Success(
        val weather: Weather,
        val warning: String? = null,        // cảnh báo nhiệt độ cao/thấp/gió mạnh
        val lastUpdated: Long = System.currentTimeMillis()
    ) : WeatherUiState()

    /** Có lỗi xảy ra */
    data class Error(
        val message: String,
        val canRetry: Boolean = true
    ) : WeatherUiState()
}