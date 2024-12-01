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
import androidx.compose.ui.res.stringResource
import com.smartshop.R
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
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.hello),
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
    var selectedOption by remember { mutableStateOf(themeOptions[if (currentTheme) 0 else 1]) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // Відступи зліва та справа
    ) {
        // Кнопка для відкриття меню
        Button(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = selectedOption)
        }

        // Випадаючий список
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp) // Відступи між меню та краями
        ) {
            themeOptions.forEach { theme ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = theme
                        onThemeChange(theme == "Темна")
                        expanded = false
                    },
                    text = { Text(theme) }
                )
            }
        }
    }
}