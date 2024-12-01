package com.smartshop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import com.smartshop.ui.theme.BackgroundDark
import com.smartshop.ui.theme.BackgroundLight
import com.smartshop.ui.theme.Dark
import com.smartshop.ui.theme.Light

@Composable
fun ProfileScreen(
    currentTheme: Boolean, // Поточна тема (темна чи світла)
    onThemeChange: (Boolean) -> Unit // Callback для зміни теми
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // Фон змінюється за темою
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Привіт",
                fontSize = 24.sp,
                color = if (currentTheme) Light else Dark
            )

            Spacer(modifier = Modifier.height(16.dp))

            DropdownMenuThemeSelector(
                currentTheme = currentTheme,
                onThemeChange = onThemeChange
            )
        }
    }
}

@Composable
fun DropdownMenuThemeSelector(
    currentTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val themeOptions = listOf("Темна", "Світла")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Кнопка, що відкриває меню для вибору теми
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (currentTheme) "Темна" else "Світла")
        }

        // Випадаюче меню для вибору теми
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            themeOptions.forEachIndexed { index, theme ->
                DropdownMenuItem(
                    onClick = {
                        onThemeChange(index == 0)
                        expanded = false
                    },
                    text = { Text(theme) }
                )
            }
        }
    }
}