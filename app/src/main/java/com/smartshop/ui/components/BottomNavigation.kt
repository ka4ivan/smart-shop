package com.smartshop.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.smartshop.R
import com.smartshop.ui.modifiers.borderBottom
import com.smartshop.ui.modifiers.borderTop

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
    val route: String,
)

@Composable
fun BottomNavigationBar(
    items: List<BottomNavigationItem>,
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit,
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = colorResource(R.color.background_bottom_menu),
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .borderTop(
                    height = 1.dp,
                    color = colorResource(R.color.light_gray),
                )
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedItemIndex == index,
                    onClick = { onItemSelected(index) },
                    label = {
                        Text(
                            text = item.title,
                            color = if (index == selectedItemIndex) {
                                colorResource(R.color.green)
                            } else {
                                colorResource(R.color.text_secondary)
                            },
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedItemIndex) {
                                item.selectedIcon
                            } else {
                                item.unselectedIcon
                            },
                            contentDescription = item.title,
                            modifier = Modifier.size(28.dp),
                            tint = if (index == selectedItemIndex) {
                                colorResource(R.color.green)
                            } else {
                                colorResource(R.color.text_secondary)
                            },
                        )
                    },
                    alwaysShowLabel = true, // Вимикає додаткове виділення меню
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    ),
                    modifier = if (index == selectedItemIndex) {
                        Modifier
                            .borderBottom(
                                height = 4.dp,
                                color = colorResource(R.color.green),
                                cornerRadius = 3.dp
                            )
                    } else {
                        Modifier
                    },
                )
            }
        }
    }
}
