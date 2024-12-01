package com.smartshop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartshop.data.model.ListData
import com.smartshop.data.repository.ListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    private val repository = ListRepository()

    private val _lists = MutableStateFlow<List<ListData>>(emptyList())
    val lists: StateFlow<List<ListData>> = _lists

    fun fetchAllLists() {
        viewModelScope.launch {
            _lists.value = repository.getAllLists()
        }
    }

    fun createList(list: ListData) {
        viewModelScope.launch {
            repository.createList(list)
            fetchAllLists()
        }
    }

    fun updateList(listId: String, updatedList: ListData) {
        viewModelScope.launch {
            repository.updateList(listId, updatedList)
            fetchAllLists()
        }
    }

    fun deleteList(listId: String) {
        viewModelScope.launch {
            repository.deleteList(listId)
            fetchAllLists()
        }
    }
}
