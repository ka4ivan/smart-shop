package com.smartshop.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.smartshop.data.model.ListitemData
import kotlinx.coroutines.tasks.await

class ListitemRepository {
    private val database = FirebaseDatabase.getInstance("https://lntu-94305-default-rtdb.europe-west1.firebasedatabase.app").reference.child("list_items")

    suspend fun createListitem(list: ListitemData) {
        val timestamp = System.currentTimeMillis()
        val listWithTimestamp = list.copy(createdAt = timestamp, updatedAt = timestamp)

        val newListRef = database.push()
        val newListWithId = listWithTimestamp.copy(id = newListRef.key ?: "")

        newListRef.setValue(newListWithId).await()
    }
}
