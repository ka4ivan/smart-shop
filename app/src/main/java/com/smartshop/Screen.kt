package com.smartshop

sealed class Screen(val route: String) {
    object ListsScreen : Screen("lists_screen")
    object CreateListScreen : Screen("create_list_screen")
    object WeatherScreen : Screen("weather_screen")
    object ProfileScreen : Screen("profile_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}