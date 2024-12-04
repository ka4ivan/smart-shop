package com.smartshop.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.smartshop.data.api.WeatherApiService
import com.smartshop.data.model.WeatherResponse
import com.smartshop.ui.theme.DarkGrayLight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import com.smartshop.BuildConfig
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.smartshop.ui.theme.BtnSuggestionBackgroundLight
import com.smartshop.ui.theme.FullWhite
import com.smartshop.ui.theme.WhiteLight

@Composable
fun WeatherScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val isNetworkAvailable = isNetworkAvailable(context)

    var city by remember { mutableStateOf("Lutsk") }

    if (!isNetworkAvailable) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "No internet connection",
                color = DarkGrayLight,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                letterSpacing = 1.5.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center
            )
        }
    } else {
        val apiKey = BuildConfig.API_KEY // Використовуємо API_KEY з BuildConfig
        var weatherResponse by remember { mutableStateOf<WeatherResponse?>(null) }
        var isLoading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var cityNotFound by remember { mutableStateOf(false) }
        var showCityInputDialog by remember { mutableStateOf(false) }

        LaunchedEffect(city) {
            if (city.isBlank()) {
                errorMessage = "City name cannot be empty"
                isLoading = false
                return@LaunchedEffect
            }

            try {
                val response = withContext(Dispatchers.IO) {
                    WeatherApiService.api.getCurrentWeather(apiKey, city.trim())
                }
                Log.d("Weather", "Response: ${response.location}")

                // Проста перевірка на null або пусті дані
                if (response == null || response.location == null) {
                    errorMessage = "City not found"
                    cityNotFound = true
                } else {
                    weatherResponse = response
                    cityNotFound = false
                }

            } catch (e: Exception) {
                errorMessage = e.localizedMessage
                cityNotFound = true
            } finally {
                isLoading = false
            }
        }

        if (isLoading) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: $errorMessage", color = Color.Red)
            }
        } else {
            weatherResponse?.let { weather ->
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Button(
                            onClick = { showCityInputDialog = true },
                            modifier = Modifier.height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BtnSuggestionBackgroundLight
                            )
                        ) {
                            Text(
                                text = "$city",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = FullWhite
                            )
                        }
                    }

                    if (showCityInputDialog) {
                        CityInputDialog(
                            onCitySelected = { newCity ->
                                city = newCity
                                showCityInputDialog = false
                            },
                            onDismiss = { showCityInputDialog = false }
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Weather in ${weather.location.name}, ${weather.location.country}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Temperature: ${weather.current.temp_c}°C")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = weather.current.condition.text)
                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            painter = rememberAsyncImagePainter("https:${weather.current.condition.icon}"),
                            contentDescription = "Weather Icon",
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            }

            // Якщо місто не знайдено
            if (cityNotFound) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "City not found",
                        color = Color.Red,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CityInputDialog(onCitySelected: (String) -> Unit, onDismiss: () -> Unit) {
    var cityInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Enter City",
                color = Color.White
            )
        },
        text = {
            OutlinedTextField(
                value = cityInput,
                onValueChange = { cityInput = it },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (cityInput.isNotBlank()) {
                        onCitySelected(cityInput)
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@SuppressLint("ObsoleteSdkInt")
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // Для Android 6.0 і вище
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        // Перевіряємо чи є доступ до Інтернету
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        // Для старіших версій Android
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}