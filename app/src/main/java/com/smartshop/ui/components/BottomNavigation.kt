package com.smartshop.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.smartshop.R

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
)

@Composable
fun BottomNavigationBar(
    items: List<BottomNavigationItem>,
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit,
) {
    NavigationBar {
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
                        }
                    )
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount != null) {
                                Badge { Text(text = item.badgeCount.toString()) }
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
}
