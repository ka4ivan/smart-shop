package com.example.smartshop.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.R

class SuggestionsAdapter(
    private val suggestions: List<String>,
    private val onSuggestionClick: (String) -> Unit
) : RecyclerView.Adapter<SuggestionsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.button_suggestion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_suggestion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val suggestion = suggestions[position]
        holder.button.text = suggestion
        holder.button.setOnClickListener {
            onSuggestionClick(suggestion)
        }
    }

    override fun getItemCount() = suggestions.size
}
