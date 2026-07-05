package com.smartshop

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.smartshop.ui.theme.SmartShopTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val preferences by lazy { getSharedPreferences("theme_prefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val isDarkTheme = preferences.getBoolean("dark_theme", false)
        val savedLanguage = preferences.getString("language", null)
            ?: if (Locale.getDefault().language == "en") "en" else "uk"

        setContent {
            var darkTheme by remember { mutableStateOf(isDarkTheme) }
            var language by remember { mutableStateOf(savedLanguage) }

            SmartShopTheme(darkTheme = darkTheme) {
                LocalizedContent(language = language) {
                    Navigation(
                        currentTheme = darkTheme,
                        onThemeChange = { newTheme ->
                            darkTheme = newTheme
                            saveThemePreference(newTheme)
                        },
                        currentLanguage = language,
                        onLanguageChange = { newLanguage ->
                            language = newLanguage
                            saveLanguagePreference(newLanguage)
                        }
                    )
                }
            }
        }
    }

    private fun saveThemePreference(isDark: Boolean) {
        preferences.edit().putBoolean("dark_theme", isDark).apply()
    }

    private fun saveLanguagePreference(language: String) {
        preferences.edit().putString("language", language).apply()
    }
}

@Composable
private fun LocalizedContent(language: String, content: @Composable () -> Unit) {
    val baseContext = LocalContext.current
    val localizedContext = remember(language) {
        val locale = Locale(language)
        val configuration = Configuration(baseContext.resources.configuration)
        configuration.setLocale(locale)
        baseContext.createConfigurationContext(configuration)
    }

    CompositionLocalProvider(LocalContext provides localizedContext) {
        content()
    }
}
