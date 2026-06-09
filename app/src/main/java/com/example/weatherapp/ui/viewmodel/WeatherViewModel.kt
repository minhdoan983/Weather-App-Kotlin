package com.example.weatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.usecase.GetWeatherByCityNameUseCase
import com.example.weatherapp.domain.usecase.GetWeatherByLocationUseCase
import com.example.weatherapp.ui.event.WeatherUiEvent
import com.example.weatherapp.ui.state.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * PRESENTATION LAYER - ViewModel
 *
 * Nhiệm vụ của ViewModel:
 *  1. Giữ UiState (sống qua configuration change như xoay màn hình)
 *  2. Nhận Event từ UI, gọi UseCase tương ứng
 *  3. Không chứa bất kỳ Android View/Context nào (dễ test)
 *
 * @HiltViewModel → Hilt tự inject constructor, không cần ViewModelFactory thủ công
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherByCity: GetWeatherByCityNameUseCase,
    private val getWeatherByLocation: GetWeatherByLocationUseCase,
) : ViewModel() {
    // StateFlow → UI observe, luôn có 1 giá trị hiện tại (không null)
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    // Search text hiện tại (riêng biệt khỏi uiState để TextField không bị giật)
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Lưu city cuối để Retry biết search lại gì
    private var lastSearchedCity: String = ""

    init {
        loadInitialData()
    }

    /**
     * Entry point cho mọi action từ UI.
     * Single function xử lý tất cả events → dễ trace flow.
     */
    fun onEvent(event: WeatherUiEvent) {
        when (event) {
            is WeatherUiEvent.SearchCity -> searchWeather(event.cityName)
            is WeatherUiEvent.UseCurrentLocation -> searchByLocation(event)
            is WeatherUiEvent.Retry -> if (lastSearchedCity.isNotBlank()) searchWeather(
                lastSearchedCity
            )

            is WeatherUiEvent.ClearSearch -> {
                _searchQuery.value = ""
                _uiState.value = WeatherUiState.Idle
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            getWeatherByLocation(10.75, 106.6667)
                .onStart { _uiState.value = WeatherUiState.Loading }
                .catch { e ->
                    _uiState.value = WeatherUiState.Error(e.message ?: "Lỗi không xác định")
                }
                .collect { result ->
                    _uiState.value = result.fold(
                        onSuccess = { weather ->
                            val warning =
                                buildWarning(weather.temperatureCelsius, weather.windSpeedMs)
                            WeatherUiState.Success(weather = weather, warning = warning)
                        },
                        onFailure = { e ->
                            WeatherUiState.Error(e.message ?: "Lỗi không xác định")
                        }
                    )
                }
        }
    }

    private fun searchWeather(cityName: String) {
        lastSearchedCity = cityName
        viewModelScope.launch {
            getWeatherByCity(cityName)
                .onStart { _uiState.value = WeatherUiState.Loading }
                .catch { e ->
                    _uiState.value = WeatherUiState.Error(e.message ?: "Lỗi không xác định")
                }
                .collect { result ->
                    _uiState.value = result.fold(
                        onSuccess = { weather ->
                            val warning =
                                buildWarning(weather.temperatureCelsius, weather.windSpeedMs)
                            WeatherUiState.Success(weather = weather, warning = warning)
                        },
                        onFailure = { e ->
                            WeatherUiState.Error(message = e.message ?: "Lỗi không xác định")
                        }
                    )
                }
        }
    }

    private fun searchByLocation(event: WeatherUiEvent.UseCurrentLocation) {
        // Trong thực tế: request permission trước, lấy FusedLocationClient
        // Đây là placeholder
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            // getWeatherByLocation(lat, lon)...
        }
    }

    private fun buildWarning(tempC: Double, windMs: Double): String? = when {
        tempC > 40 -> "⚠️ Nhiệt độ cực cao, hạn chế ra ngoài!"
        tempC < 0 -> "⚠️ Băng giá, cẩn thận trơn trượt!"
        windMs > 20 -> "⚠️ Gió mạnh, cẩn thận khi ra ngoài!"
        else -> null
    }
}