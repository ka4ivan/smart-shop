package com.smartshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.smartshop.ui.components.BottomNavigationBar
import com.smartshop.ui.components.BottomNavigationItem
import com.smartshop.ui.screens.ListsScreen
import com.smartshop.ui.screens.ProfileScreen
import com.smartshop.ui.screens.WeatherScreen
import com.smartshop.ui.theme.SmartShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartShopTheme {
                val items = listOf(
                    BottomNavigationItem(
                        title = stringResource(R.string.lists),
                        selectedIcon = ImageVector.vectorResource(R.drawable.lists),
                        unselectedIcon = ImageVector.vectorResource(R.drawable.lists),
                        hasNews = false,
                    ),
                    BottomNavigationItem(
                        title = stringResource(R.string.weather),
                        selectedIcon = ImageVector.vectorResource(R.drawable.weather),
                        unselectedIcon = ImageVector.vectorResource(R.drawable.weather),
                        hasNews = false,
                    ),
                    BottomNavigationItem(
                        title = stringResource(R.string.profile),
                        selectedIcon = ImageVector.vectorResource(R.drawable.user),
                        unselectedIcon = ImageVector.vectorResource(R.drawable.user),
                        hasNews = false,
                    ),
                )

                var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

                Scaffold (
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(
                            items = items,
                            selectedItemIndex = selectedItemIndex,
                            onItemSelected = { selectedItemIndex = it }
                        )
                    }
                ) { innerPadding ->
                    when (selectedItemIndex) {
                        0 -> ListsScreen(Modifier.padding(innerPadding))
                        1 -> WeatherScreen(Modifier.padding(innerPadding))
                        2 -> ProfileScreen(Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}
