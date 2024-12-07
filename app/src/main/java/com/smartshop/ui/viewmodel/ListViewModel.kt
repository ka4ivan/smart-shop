package com.smartshop.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartshop.data.model.ListData
import com.smartshop.data.model.ListitemData
import com.smartshop.data.repository.ListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    private val repository = ListRepository()

    private val _lists = MutableStateFlow<List<ListData>>(emptyList())
    val lists: StateFlow<List<ListData>> = _lists

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    suspend fun getListsOnce(userId: String): List<ListData> {
        return repository.getListsOnce(userId)
    }

    suspend fun showList(listId: String): ListData {
        return repository.showList(listId)
    }

    fun loadDeletedLists(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val lists = repository.getDeletedListsOnce(userId)
            _lists.value = lists
            _isLoading.value = false
        }
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

    fun createList(list: ListData, onComplete: (ListData) -> Unit) {
        viewModelScope.launch {
            val newList = repository.createList(list)
            fetchAllLists()
            onComplete(newList)
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

    fun permanentlyDeleteItem(listId: String) {
        viewModelScope.launch {
            repository.forceDeleteList(listId)
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

    fun undoListitem(listitemId: String) {
        viewModelScope.launch {
            repository.undoListitem(listitemId)
            fetchAllLists()
        }
    }

    suspend fun getListitems(listId: String): List<ListitemData> {
        return repository.getListitems(listId)
    }

    fun updateListitem(listitemId: String, updatedListitem: ListitemData) {
        viewModelScope.launch {
            repository.updateListitem(listitemId, updatedListitem)
        }
    }

    fun deleteListitem(listitemId: String) {
        viewModelScope.launch {
            repository.deleteListitem(listitemId)
        }
    }

    fun removeFromTrash(listId: String) {
        viewModelScope.launch {
            repository.undoList(listId)
        }
    }

    fun uncheckListitems(listId: String) {
        viewModelScope.launch {
            repository.uncheckListitems(listId)
        }
    }

    fun deleteCheckedListitems(listId: String) {
        viewModelScope.launch {
            repository.deleteCheckedListitems(listId)
        }
    }
}
