package com.example.smartshop.ui.notifications

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

        // Створюємо адаптер для Spinner
        val themeOptions = arrayOf("Чорний", "Білий")
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, themeOptions) // Використання кастомного layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Фон для випадаючого меню
        themeSelector.adapter = adapter

        // Обробник вибору теми
        themeSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> applyTheme(AppCompatDelegate.MODE_NIGHT_YES) // Чорний
                    1 -> applyTheme(AppCompatDelegate.MODE_NIGHT_NO) // Білий
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return rootView
    }

    private fun applyTheme(themeMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }
}