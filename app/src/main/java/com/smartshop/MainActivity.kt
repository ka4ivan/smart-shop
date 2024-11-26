package com.smartshop

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smartshop.ui.theme.SmartShopTheme

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
)

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
                var selectedItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }

                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    NavigationBar {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedItemIndex == index,
                                onClick = {
                                    selectedItemIndex = index
//                                    navController.navigate(item.title)
                                },
                                label = {
                                    Text(text = item.title,
                                        color = if (index == selectedItemIndex) {
                                            colorResource(R.color.green)
                                        } else {
                                            colorResource(R.color.text_secondary)
                                        })
                                },
                                icon = {
                                    BadgedBox(
                                        badge = {
                                            if (item.badgeCount != null) {
                                                Badge {
                                                    Text(text = item.badgeCount.toString())
                                                }
                                            } else if (item.hasNews) {
                                                Badge()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (index == selectedItemIndex) {
                                                item.selectedIcon
                                            } else item.unselectedIcon,
                                            contentDescription = item.title,
                                            modifier = Modifier.size(28.dp),
                                            tint = if (index == selectedItemIndex) {
                                                colorResource(R.color.green)
                                            } else {
                                                colorResource(R.color.text_secondary)
                                            },
                                        )
                                    }
                                },
                            )
                        }
                    }
                }) { innerPadding ->
                    Greeting(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    "Greeting".logD()
    Column {
        var name by remember { mutableStateOf("") }

        Text(
            text = "Hello $name!",
            modifier = modifier
        )

        OutlinedTextField(
            value = name,
            onValueChange = { newName: String ->
                "newName: $newName".logD()
                name = newName
            },
            label = { Text("Name") }
        )
    }
}

fun String.logD() {
    Log.d("SmartShop", this)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartShopTheme {
        Greeting()
    }
}