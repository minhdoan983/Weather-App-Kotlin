package com.example.weatherapp.data.repository

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.mapper.toDomain
import com.example.weatherapp.data.remote.api.WeatherApiService
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApiService,
) : WeatherRepository {
    override fun getWeatherByCityName(cityName: String): Flow<Result<Weather>> = flow {
        try {
            val dto = api.getWeatherByCity(cityName = cityName, apiKey = API_KEY)

            val weather = dto.toDomain()

            emit(Result.success(weather))
        } catch (e: retrofit2.HttpException) {
            val message = when (e.code()) {
                401 -> "API key không hợp lệ"
                404 -> "Không tìm thấy thành phố \"$cityName\""
                429 -> "Quá nhiều request, thử lại sau"
                else -> "Lỗi server: ${e.code()}"
            }
            emit(Result.failure(Exception(message)))

        } catch (e: java.io.IOException) {
            emit(Result.failure(Exception("Không có kết nối mạng")))

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getWeatherByLocation(lat: Double, lon: Double): Flow<Result<Weather>> = flow {
        try {
            val dto = api.getWeatherByLocation(lat = lat, lon = lon, apiKey = API_KEY)
            emit(Result.success(dto.toDomain()))
        } catch (e: Exception) {
            emit(Result.failure(Exception("Không lấy được thời tiết theo vị trí")))
        }
    }

    companion object {
        // Trong thực tế: đọc từ BuildConfig.API_KEY hoặc local.properties
        // KHÔNG hardcode API key trong source code thật
        private const val API_KEY = BuildConfig.WEATHER_API_KEY
    }
}