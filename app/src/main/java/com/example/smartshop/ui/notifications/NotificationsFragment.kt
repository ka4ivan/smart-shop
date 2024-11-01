package com.example.smartshop.ui.notifications

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.smartshop.R
import androidx.appcompat.app.AppCompatDelegate

class NotificationsFragment : Fragment() {

    private lateinit var themeSelector: Spinner
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_notifications, container, false)
        themeSelector = rootView.findViewById(R.id.theme_selector)

        // Завантаження збереженої теми при створенні фрагмента
        loadThemePreference()

        // Створюємо адаптер для Spinner
        val themeOptions = arrayOf("Темна", "Світла")
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, themeOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        themeSelector.adapter = adapter

        // Встановлення початкового вибору відповідно до збереженої теми
        themeSelector.setSelection(if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) 0 else 1)

        // Обробник вибору теми
        themeSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val themeMode = if (position == 0) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                if (AppCompatDelegate.getDefaultNightMode() != themeMode) {
                    applyTheme(themeMode)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return rootView
    }

    private fun applyTheme(themeMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveThemePreference(themeMode)
    }

    private fun saveThemePreference(themeMode: Int) {
        val prefs = requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("theme_mode", themeMode).apply()
    }

    private fun loadThemePreference() {
        val prefs = requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val savedThemeMode = prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO)
        AppCompatDelegate.setDefaultNightMode(savedThemeMode)
    }
}
