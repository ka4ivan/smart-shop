package com.example.smartshop.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.R

class NewListDialogFragment : DialogFragment() {

    private lateinit var suggestionsAdapter: SuggestionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        // кнопка повернення
        val backButton: View? = dialog?.findViewById(R.id.dtn_back_from_create_list)
        backButton?.setOnClickListener {
            dismiss()
            // якщо треба повернутися до HomeFragment
            // requireActivity().supportFragmentManager.popBackStack()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val suggestions = listOf("Продукти", "Їжа", "Ліки", "Вихідні", "П'ятниця", "Поїздка", "Супермаркет", "Дім", "У подорож")
        val recyclerView: RecyclerView = dialog?.findViewById(R.id.recycler_view_suggestions)!!

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        suggestionsAdapter = SuggestionsAdapter(suggestions) { suggestion ->
            val textInput = dialog?.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.input_new_list)
            textInput?.setText(suggestion)
        }
        recyclerView.adapter = suggestionsAdapter
    }


    override fun getTheme(): Int {
        return R.style.DialogSlideAnimation
    }
}
