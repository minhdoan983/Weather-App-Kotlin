package com.example.weatherapp.ui.presentation

import com.example.weatherapp.ui.viewmodel.WeatherViewModel
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.model.WeatherCondition
import com.example.weatherapp.ui.event.WeatherUiEvent
import com.example.weatherapp.ui.state.WeatherUiState

/**
 * PRESENTATION LAYER - Home Screen
 *
 * Composable KHÔNG chứa logic — chỉ:
 *  1. Observe state từ ViewModel
 *  2. Render UI tương ứng với state
 *  3. Gửi Event lên ViewModel khi user tương tác
 *
 * hiltViewModel() → Hilt tạo và inject ViewModel tự động
 */
@Composable
fun HomeScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    modifier: Modifier
) {
    // collectAsState() → Composable re-compose khi StateFlow emit giá trị mới
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Search bar
        SearchBar(
            query = searchQuery,
            onQueryChange = viewModel::onSearchQueryChange,
            onSearch = { viewModel.onEvent(WeatherUiEvent.SearchCity(searchQuery)) },
            onLocationClick = { viewModel.onEvent(WeatherUiEvent.UseCurrentLocation) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // AnimatedContent → smooth transition giữa các states
        AnimatedContent(
            targetState = uiState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "weather_state"
        ) { state ->
            when (state) {
                is WeatherUiState.Idle -> IdleContent()
                is WeatherUiState.Loading -> LoadingContent()
                is WeatherUiState.Success -> WeatherContent(
                    weather = state.weather,
                    warning = state.warning
                )

                is WeatherUiState.Error -> ErrorContent(
                    message = state.message,
                    canRetry = state.canRetry,
                    onRetry = { viewModel.onEvent(WeatherUiEvent.Retry) }
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onLocationClick: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Nhập tên thành phố...") },
        leadingIcon = {
        },
        trailingIcon = {
            Row {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onSearch() }) {
                    }
                }
                IconButton(onClick = onLocationClick) {
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
private fun WeatherContent(weather: Weather, warning: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        // Warning banner (nếu có)
        warning?.let {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Tên thành phố
        Text(
            text = weather.cityName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Icon thời tiết
        Text(
            text = weather.condition.toEmoji(),
            fontSize = 80.sp
        )

        // Nhiệt độ
        Text(
            text = "${weather.temperatureCelsius.toInt()}°C",
            fontSize = 64.sp,
            fontWeight = FontWeight.Light
        )

        Text(
            text = weather.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Thông tin chi tiết
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeatherDetailItem(icon = "💧", label = "Độ ẩm", value = "${weather.humidity}%")
            WeatherDetailItem(
                icon = "🌡️",
                label = "Cảm giác",
                value = "${weather.feelsLikeCelsius.toInt()}°C"
            )
            WeatherDetailItem(
                icon = "💨",
                label = "Gió",
                value = "${weather.windSpeedMs.toInt()} m/s"
            )
        }
    }
}

@Composable
private fun WeatherDetailItem(icon: String, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 24.sp)
        Text(text = value, fontWeight = FontWeight.SemiBold)
        Text(
            text = label, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LoadingContent() {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun IdleContent() {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🌤️", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Tìm kiếm thành phố để xem thời tiết",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorContent(message: String, canRetry: Boolean, onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
        Text("❌", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(message, color = MaterialTheme.colorScheme.error)
        if (canRetry) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) { Text("Thử lại") }
        }
    }
}

// Extension function: chuyển enum → emoji để hiển thị
private fun WeatherCondition.toEmoji(): String = when (this) {
    WeatherCondition.SUNNY -> "☀️"
    WeatherCondition.CLOUDY -> "⛅"
    WeatherCondition.RAINY -> "🌧️"
    WeatherCondition.STORMY -> "⛈️"
    WeatherCondition.SNOWY -> "❄️"
    WeatherCondition.FOGGY -> "🌫️"
    WeatherCondition.UNKNOWN -> "🌤️"
}