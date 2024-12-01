package com.smartshop.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.smartshop.ui.modifiers.borderBottom
import com.smartshop.ui.modifiers.borderTop
import com.smartshop.ui.theme.LocalCustomColors

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
        containerColor = LocalCustomColors.current.bottomMenuBackground,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .borderTop(
                    height = 1.dp,
                    color = LocalCustomColors.current.lightGray,
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
                                LocalCustomColors.current.green
                            } else {
                                LocalCustomColors.current.textSecondary
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
                                LocalCustomColors.current.green
                            } else {
                                LocalCustomColors.current.textSecondary
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
                                color = LocalCustomColors.current.green,
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
