package com.example.smartshop.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.MainDb
import com.example.smartshop.R
import com.example.smartshop.db.Entities.Shoppinglist
import com.google.android.material.textfield.TextInputEditText

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

        val db = MainDb.getDb(requireContext())

        // кнопка повернення
        val backButton: View? = dialog?.findViewById(R.id.dtn_back_from_create_list)
        backButton?.setOnClickListener {
            dismiss()
        }

        setupRecyclerView()

        // кнопка створення списку
        val createButton: View? = dialog?.findViewById(R.id.btn_create_list)
        createButton?.setOnClickListener {
            val inputField = dialog?.findViewById<TextInputEditText>(R.id.input_new_list)
            val listName = inputField?.text.toString()

            val shoppinglist = Shoppinglist(null, listName, false)

            Thread {
                db.getDao().insertShoppinglist(shoppinglist)
            }.start()

            if (listName.isNotBlank()) {
                Toast.makeText(requireContext(), "Список \"$listName\" створено", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Введіть назву списку", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        val suggestions = listOf("Продукти", "Їжа", "Ліки", "Вихідні", "П'ятниця", "Поїздка", "Супермаркет", "Дім", "У подорож")
        val recyclerView: RecyclerView = dialog?.findViewById(R.id.recycler_view_suggestions)!!

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        suggestionsAdapter = SuggestionsAdapter(suggestions) { suggestion ->
            val textInput = dialog?.findViewById<TextInputEditText>(R.id.input_new_list)
            textInput?.setText(suggestion)
        }
        recyclerView.adapter = suggestionsAdapter
    }

    override fun getTheme(): Int {
        return R.style.DialogSlideAnimation
    }
}
