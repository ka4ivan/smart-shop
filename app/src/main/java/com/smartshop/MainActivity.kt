package com.smartshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.smartshop.ui.theme.SmartShopTheme

class MainActivity : ComponentActivity() {
    private val preferences by lazy { getSharedPreferences("theme_prefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val isDarkTheme = preferences.getBoolean("dark_theme", false)

        setContent {
            var darkTheme by remember { mutableStateOf(isDarkTheme) }

            // Застосування теми
            SmartShopTheme(darkTheme = darkTheme) {
                Navigation(
                    currentTheme = darkTheme,
                    onThemeChange = { newTheme ->
                        darkTheme = newTheme
                        saveThemePreference(newTheme)
                    }
                )
            }
        }
    }

    private fun saveThemePreference(isDark: Boolean) {
        preferences.edit().putBoolean("dark_theme", isDark).apply()
    }
}