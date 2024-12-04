package com.smartshop

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import com.smartshop.ui.screens.CreateListitemScreen
import com.smartshop.ui.screens.ListScreen
import com.smartshop.ui.screens.ListsScreen
import com.smartshop.ui.screens.ProfileScreen
import com.smartshop.ui.screens.TrashScreen
import com.smartshop.ui.screens.WeatherScreen
import com.smartshop.ui.viewmodel.ListViewModel
import com.smartshop.ui.viewmodel.ListitemViewModel

@Composable
fun Navigation(
    currentTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
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

    val selectedItemIndex = items.indexOfFirst { (it.route == currentRoute || (currentRoute == Screen.TrashScreen.route && it.route == Screen.ListsScreen.route)) }

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.CreateListScreen.route && currentRoute != Screen.ListScreen.route && currentRoute != Screen.CreateListitemScreen.route) {
                BottomNavigationBar(
                    items = items,
                    selectedItemIndex = selectedItemIndex,
                    onItemSelected = { index ->
                        val route = items[index].route
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.ListsScreen.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                scaleIn(
                    initialScale = 0.8f, 
                    animationSpec = tween(225)
                ) + fadeIn(animationSpec = tween(225))
            },
            exitTransition = {
                scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(225)
                ) + fadeOut(animationSpec = tween(225))
            },
            popEnterTransition = {
                scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(225)
                ) + fadeIn(animationSpec = tween(225))
            },
            popExitTransition = {
                scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(225)
                ) + fadeOut(animationSpec = tween(225))
            }
        ) {
            composable(Screen.ListsScreen.route) { ListsScreen(
                navController = navController,
                viewModel = ListViewModel(),
            ) }
            composable("list_screen/{listId}") { backStackEntry ->
                val listId = backStackEntry.arguments?.getString("listId") ?: ""
                ListScreen(
                    navController = navController,
                    viewModel = ListViewModel(),
                    listId = listId)
            }
            composable(Screen.CreateListScreen.route) { CreateListScreen(
                navController = navController,
                viewModel = ListViewModel(),
            ) }
            composable(Screen.CreateListitemScreen.route) { backStackEntry ->
                val listId = backStackEntry.arguments?.getString("listId") ?: ""
                CreateListitemScreen(
                    navController = navController,
                    viewModel = ListitemViewModel(),
                    listId = listId)
            }
            composable(Screen.WeatherScreen.route) { WeatherScreen() }
            composable(Screen.ProfileScreen.route) {
                ProfileScreen(
                    currentTheme = currentTheme,
                    onThemeChange = onThemeChange
                )
            }
            composable(Screen.TrashScreen.route) { TrashScreen(navController = navController) }
        }
    }
}