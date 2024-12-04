package com.smartshop.ui.viewmodel

import android.util.Log
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

    suspend fun getListsOnce(userId: String): List<ListData> {
        return repository.getListsOnce(userId)
    }

    suspend fun showList(listId: String): ListData {
        return repository.showList(listId)
    }

    fun fetchAllLists() {
        viewModelScope.launch {
            _lists.value = repository.getAllLists()
        }
    }

    fun getLists(userId: String) {
        viewModelScope.launch {
            val lists = repository.getLists(userId)
            Log.d("ListViewModel", "Fetched lists: $lists")
            _lists.value = lists
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

    fun forceDeleteList(listId: String) {
        viewModelScope.launch {
            repository.forceDeleteList(listId)
            fetchAllLists()
        }
    }

    fun deleteList(listId: String) {
        viewModelScope.launch {
            repository.deleteList(listId)
            fetchAllLists()
        }
    }

    fun undoList(listId: String) {
        viewModelScope.launch {
            repository.undoList(listId)
            fetchAllLists()
        }
    }
}
