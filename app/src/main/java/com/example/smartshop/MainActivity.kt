package com.example.smartshop

import android.os.Bundle
import android.content.Context
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smartshop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var isDarkTheme = false

    private fun toggleTheme() {
        if (isDarkTheme) {
            setTheme(R.style.AppTheme_Light) // Світла тема
        } else {
            setTheme(R.style.AppTheme_Dark) // Темна тема
        }
        recreate() // Перезавантажте активність для застосування нової теми
        isDarkTheme = !isDarkTheme // Перемикаємо прапорець теми
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Отримати збережену тему
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val currentTheme = sharedPref.getString("theme", "Білий") // "Білий" за замовчуванням

        // Встановіть тему перед викликом super.onCreate()
        setTheme(if (currentTheme == "Чорний") R.style.AppTheme_Dark else R.style.AppTheme_Light)

        super.onCreate(savedInstanceState)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        // Установка теми при старті програми
        if (isDarkTheme) {
            setTheme(R.style.AppTheme_Dark)
        } else {
            setTheme(R.style.AppTheme_Light)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu) // Переконайтеся, що шлях правильний
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_theme -> { // Перемикання теми
                toggleTheme()
                true
            }
            R.id.action_trash -> {
                // Код для обробки натискання на "Смітник"
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}