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

    fun removeListitem(name: String, listId: String, qty: Double = 1.0) {
        viewModelScope.launch {
            repository.removeListitem(name, listId, qty)
        }
    }

    fun deleteListitem(listitem: ListitemData) {
        viewModelScope.launch {
//            repository.deleteListitem(listitem)
        }
    }

    suspend fun getListitemsOnce(listitemId: String): List<ListitemData> {
        return repository.getListitemsOnce(listitemId)
    }
}
