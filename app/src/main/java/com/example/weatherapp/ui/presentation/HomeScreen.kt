import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.R
import com.example.weatherapp.ui.state.WeatherUiState
import com.example.weatherapp.ui.theme.Green
import com.example.weatherapp.ui.theme.White
import com.example.weatherapp.ui.viewmodel.WeatherViewModel


//package com.example.weatherapp.ui.presentation
//
//import com.example.weatherapp.ui.viewmodel.WeatherViewModel
//import androidx.compose.animation.AnimatedContent
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.togetherWith
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.systemBarsPadding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
//import com.example.weatherapp.domain.model.Weather
//import com.example.weatherapp.domain.model.WeatherCondition
//import com.example.weatherapp.ui.event.WeatherUiEvent
//import com.example.weatherapp.ui.state.WeatherUiState
//
///**
// * PRESENTATION LAYER - Home Screen
// *
// * Composable KHÔNG chứa logic — chỉ:
// *  1. Observe state từ ViewModel
// *  2. Render UI tương ứng với state
// *  3. Gửi Event lên ViewModel khi user tương tác
// *
// * hiltViewModel() → Hilt tạo và inject ViewModel tự động
// */
//@Composable
//fun HomeScreen(
//    viewModel: WeatherViewModel = hiltViewModel(),
//    modifier: Modifier
//) {
//    // collectAsState() → Composable re-compose khi StateFlow emit giá trị mới
//    val uiState by viewModel.uiState.collectAsState()
//    val searchQuery by viewModel.searchQuery.collectAsState()
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(horizontal = 16.dp)
//            .systemBarsPadding(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Search bar
//        SearchBar(
//            query = searchQuery,
//            onQueryChange = viewModel::onSearchQueryChange,
//            onSearch = { viewModel.onEvent(WeatherUiEvent.SearchCity(searchQuery)) },
//            onLocationClick = { viewModel.onEvent(WeatherUiEvent.UseCurrentLocation) }
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // AnimatedContent → smooth transition giữa các states
//        AnimatedContent(
//            targetState = uiState,
//            transitionSpec = { fadeIn() togetherWith fadeOut() },
//            label = "weather_state"
//        ) { state ->
//            when (state) {
//                is WeatherUiState.Idle -> IdleContent()
//                is WeatherUiState.Loading -> LoadingContent()
//                is WeatherUiState.Success -> WeatherContent(
//                    weather = state.weather,
//                    warning = state.warning
//                )
//
//                is WeatherUiState.Error -> ErrorContent(
//                    message = state.message,
//                    canRetry = state.canRetry,
//                    onRetry = { viewModel.onEvent(WeatherUiEvent.Retry) }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun SearchBar(
//    query: String,
//    onQueryChange: (String) -> Unit,
//    onSearch: () -> Unit,
//    onLocationClick: () -> Unit
//) {
//    OutlinedTextField(
//        value = query,
//        onValueChange = onQueryChange,
//        modifier = Modifier.fillMaxWidth(),
//        placeholder = { Text("Nhập tên thành phố...") },
//        leadingIcon = {
//        },
//        trailingIcon = {
//            Row {
//                if (query.isNotEmpty()) {
//                    IconButton(onClick = { onSearch() }) {
//                    }
//                }
//                IconButton(onClick = onLocationClick) {
//                }
//            }
//        },
//        singleLine = true,
//        shape = RoundedCornerShape(12.dp)
//    )
//}
//
//@Composable
//private fun WeatherContent(weather: Weather, warning: String?) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//
//        // Warning banner (nếu có)
//        warning?.let {
//            Card(
//                colors = CardDefaults.cardColors(
//                    containerColor = MaterialTheme.colorScheme.errorContainer
//                ),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = it,
//                    modifier = Modifier.padding(12.dp),
//                    color = MaterialTheme.colorScheme.onErrorContainer
//                )
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//
//        // Tên thành phố
//        Text(
//            text = weather.cityName,
//            fontSize = 28.sp,
//            fontWeight = FontWeight.Bold
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Icon thời tiết
//        Text(
//            text = weather.condition.toEmoji(),
//            fontSize = 80.sp
//        )
//
//        // Nhiệt độ
//        Text(
//            text = "${weather.temperatureCelsius.toInt()}°C",
//            fontSize = 64.sp,
//            fontWeight = FontWeight.Light
//        )
//
//        Text(
//            text = weather.description,
//            style = MaterialTheme.typography.bodyLarge,
//            color = MaterialTheme.colorScheme.onSurfaceVariant
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Thông tin chi tiết
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            WeatherDetailItem(icon = "💧", label = "Độ ẩm", value = "${weather.humidity}%")
//            WeatherDetailItem(
//                icon = "🌡️",
//                label = "Cảm giác",
//                value = "${weather.feelsLikeCelsius.toInt()}°C"
//            )
//            WeatherDetailItem(
//                icon = "💨",
//                label = "Gió",
//                value = "${weather.windSpeedMs.toInt()} m/s"
//            )
//        }
//    }
//}
//
//@Composable
//private fun WeatherDetailItem(icon: String, label: String, value: String) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(text = icon, fontSize = 24.sp)
//        Text(text = value, fontWeight = FontWeight.SemiBold)
//        Text(
//            text = label, style = MaterialTheme.typography.bodySmall,
//            color = MaterialTheme.colorScheme.onSurfaceVariant
//        )
//    }
//}
//
//@Composable
//private fun LoadingContent() {
//    Box(
//        contentAlignment = Alignment.Center, modifier = Modifier
//            .fillMaxWidth()
//            .height(300.dp)
//    ) {
//        CircularProgressIndicator()
//    }
//}
//
//@Composable
//private fun IdleContent() {
//    Box(
//        contentAlignment = Alignment.Center, modifier = Modifier
//            .fillMaxWidth()
//            .height(300.dp)
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Text("🌤️", fontSize = 64.sp)
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                "Tìm kiếm thành phố để xem thời tiết",
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//        }
//    }
//}
//
//@Composable
//private fun ErrorContent(message: String, canRetry: Boolean, onRetry: () -> Unit) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
//        Text("❌", fontSize = 48.sp)
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(message, color = MaterialTheme.colorScheme.error)
//        if (canRetry) {
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(onClick = onRetry) { Text("Thử lại") }
//        }
//    }
//}
//
//// Extension function: chuyển enum → emoji để hiển thị
//private fun WeatherCondition.toEmoji(): String = when (this) {
//    WeatherCondition.SUNNY -> "☀️"
//    WeatherCondition.CLOUDY -> "⛅"
//    WeatherCondition.RAINY -> "🌧️"
//    WeatherCondition.STORMY -> "⛈️"
//    WeatherCondition.SNOWY -> "❄️"
//    WeatherCondition.FOGGY -> "🌫️"
//    WeatherCondition.UNKNOWN -> "🌤️"
//}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreen(viewModel: WeatherViewModel = hiltViewModel()) {


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    innerPadding
                )
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Good moring, Alex",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
                IconButton(
                    shape = CircleShape,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Green),
                    onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = White
                    )
                }
            }
            when (val state = uiState) {
                is WeatherUiState.Error -> {}
                is WeatherUiState.Idle -> {}
                is WeatherUiState.Loading -> {}
                is WeatherUiState.Success -> {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        color = Green,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        "${state.weather.temperatureCelsius}°C",
                                        style = TextStyle(
                                            fontSize = 38.sp,
                                            color = White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        state.weather.condition.toString(),
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            color = White
                                        )
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.Bottom)
                                        .padding(bottom = 28.dp)
                                ) {
                                    Text(
                                        "20%", style = TextStyle(
                                            fontSize = 18.sp,
                                            color = White
                                        )
                                    )
                                    Text(
                                        "Change of rain",
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            color = White
                                        )
                                    )
                                }
                                Icon(
                                    painter = painterResource(id = R.drawable.sunny),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.padding(16.dp)
                                )

                            }
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = Color(alpha = 0.1f, red = 1.0f, green = 1.0f, blue = 1.0f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(
                                        horizontal = 20.dp,
                                        vertical = 16.dp
                                    )
                                ) {
                                    Text(
                                        text = "Great day for outdoor tasks!",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color(0xFFE8F0E9)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Low rain chance and mild temperatures.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFA8BFAA)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .padding(vertical = 16.dp, horizontal = 10.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                WeatherHour()
                                WeatherHour()
                                WeatherHour()
                                WeatherHour()
                                WeatherHour()
                                WeatherHour()
                            }

                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Today's Task",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
                Text("View all", style = TextStyle(color = Green, fontWeight = FontWeight.Medium))
            }
            ToDoCard()
        }
    }
}

@Composable
fun WeatherHour() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Now", style = TextStyle(color = White))
        Spacer(modifier = Modifier.height(8.dp))
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(id = R.drawable.sunny),
            contentDescription = null,
            tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("24°", style = TextStyle(color = White, fontSize = 22.sp))
    }
}

@Composable
fun ToDoCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        color = Color.White,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 18.dp, horizontal = 10.dp)
        ) {
            RadioButton(selected = false, onClick = {})
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = Color.LightGray)
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    "Water the garden",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        Text("Outdoor")
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("9:00 AM", style = TextStyle(color = Color.LightGray))
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFFD1802E), RoundedCornerShape(8.dp))
                            .background(color = Color(0xFFF8EED1))
                            .padding(6.dp)
                    ) {
                        Text("Medium", style = TextStyle(color = Color(0xFFD1802E)))
                    }
                }

            }

        }
    }
}