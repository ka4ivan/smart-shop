package com.smartshop

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smartshop.ui.components.BottomNavigationBar
import com.smartshop.ui.components.BottomNavigationItem
import com.smartshop.ui.screens.CreateListScreen
import com.smartshop.ui.screens.ListsScreen
import com.smartshop.ui.screens.ProfileScreen
import com.smartshop.ui.screens.WeatherScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavigationItem(
            title = stringResource(R.string.lists),
            selectedIcon = ImageVector.vectorResource(R.drawable.lists),
            unselectedIcon = ImageVector.vectorResource(R.drawable.lists),
            hasNews = false,
            route = Screen.ListsScreen.route
        ),
        BottomNavigationItem(
            title = stringResource(R.string.weather),
            selectedIcon = ImageVector.vectorResource(R.drawable.weather),
            unselectedIcon = ImageVector.vectorResource(R.drawable.weather),
            hasNews = false,
            route = Screen.WeatherScreen.route
        ),
        BottomNavigationItem(
            title = stringResource(R.string.profile),
            selectedIcon = ImageVector.vectorResource(R.drawable.user),
            unselectedIcon = ImageVector.vectorResource(R.drawable.user),
            hasNews = false,
            route = Screen.ProfileScreen.route
        ),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedItemIndex = items.indexOfFirst { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = items,
                selectedItemIndex = selectedItemIndex,
                onItemSelected = { index ->
                    val route = items[index].route
                    if (currentRoute != route) { // Уникнення повторної навігації
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.ListsScreen.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.ListsScreen.route) { ListsScreen(navController = navController) }
            composable(Screen.CreateListScreen.route) { CreateListScreen() }
            composable(Screen.WeatherScreen.route) { WeatherScreen() }
            composable(Screen.ProfileScreen.route) { ProfileScreen() }
        }
    }
}
