package com.smartshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*

import com.smartshop.ui.theme.SmartShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkTheme = remember { mutableStateOf(false) }

            // Застосування теми
            SmartShopTheme(darkTheme = darkTheme.value) {
                Navigation(
                    currentTheme = darkTheme.value,
                    onThemeChange = { newTheme ->
                        darkTheme.value = newTheme
                    }
                )
            }
        }
    }
}