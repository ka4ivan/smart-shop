package com.example.smartshop.ui.notifications

import android.content.Intent
import android.net.Uri
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
import com.example.smartshop.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private lateinit var themeSelector: Spinner
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Інфляція макету фрагмента
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        // Отримання кореневого вигляду з binding
        val rootView = binding.root

        // Завантаження збереженої теми при створенні фрагмента
        loadThemePreference()

        // Налаштування Spinner для вибору теми
        themeSelector = binding.themeSelector // Використання binding для доступу до Spinner
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

        // Налаштування кліка для політики конфіденційності
        binding.privacyPolicy.setOnClickListener {
            openWebPage("https://github.com/ka4ivan")
        }

        // Налаштування кліка для умови використання
        binding.termsOfService.setOnClickListener {
            openWebPage("https://github.com/nezocks")
        }

        return rootView
    }

    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
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
