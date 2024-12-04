package com.smartshop.data.model

data class WeatherResponse(
    val location: Location,
    val current: Current
)

data class Location(
    val name: String,
    val region: String,
    val country: String
)

data class Current(
    val temp_c: Float,       // Температура в градусах Цельсія
    val condition: Condition // Умови погоди
)

data class Condition(
    val text: String,        // Опис погоди, наприклад, "Clear"
    val icon: String         // URL іконки погоди
)