package com.smartshop

sealed class Screen(val route: String) {
    object ListsScreen : Screen("lists_screen")
    object ListScreen : Screen("list_screen/{listId}")
    object ListitemScreen : Screen("listitem_screen/{listitemId}")
    object CreateListScreen : Screen("create_list_screen")
    object CreateListitemScreen : Screen("create_listitem_screen/{listId}")
    object WeatherScreen : Screen("weather_screen")
    object ProfileScreen : Screen("profile_screen")
    object TrashScreen : Screen("trash_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}