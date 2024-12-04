package com.smartshop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartshop.data.model.ListitemData
import com.smartshop.data.repository.ListitemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListitemViewModel : ViewModel() {
    private val repository = ListitemRepository()

    private val _listitems = MutableStateFlow<List<ListitemData>>(emptyList())
    val listitems: StateFlow<List<ListitemData>> = _listitems

    fun createListitem(listitem: ListitemData) {
        viewModelScope.launch {
            repository.createListitem(listitem)
        }
    }
}
