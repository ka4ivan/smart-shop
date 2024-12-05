package com.smartshop.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.smartshop.data.model.ListitemData
import kotlinx.coroutines.tasks.await

class ListitemRepository {
    private val database = FirebaseDatabase.getInstance("https://lntu-94305-default-rtdb.europe-west1.firebasedatabase.app").reference.child("listitems")

    suspend fun createListitem(list: ListitemData) {
        val existingItemQuery = database
            .orderByChild("listId")
            .equalTo(list.listId)
            .get()
            .await()

        val matchingItem = existingItemQuery.children.firstOrNull {
            it.child("name").value == list.name
        }

        if (matchingItem != null) {
            val existingQty = matchingItem.child("qty").value.toString().toDoubleOrNull() ?: 0.0
            val newQty = existingQty + 1

            val updatedData = mapOf(
                "qty" to newQty,
                "updatedAt" to System.currentTimeMillis()
            )
            matchingItem.ref.updateChildren(updatedData).await()
        } else {
            val timestamp = System.currentTimeMillis()
            val listWithTimestamp = list.copy(createdAt = timestamp, updatedAt = timestamp)

            val newListRef = database.push()
            val newListWithId = listWithTimestamp.copy(id = newListRef.key ?: "")

            newListRef.setValue(newListWithId).await()
        }
    }

    suspend fun getListitemsOnce(listId: String): List<ListitemData> {
        return try {
            val snapshot = database
                .orderByChild("listId")
                .equalTo(listId)
                .get()
                .await()

            val lists = snapshot.children.mapNotNull {
                val listData = it.getValue(ListitemData::class.java)
                listData
            }

            lists.filterNot { it.delete == true }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun removeListitem(name: String, listId: String, qty: Double = 1.0) {
        try {
            val snapshot = database
                .orderByChild("listId")
                .equalTo(listId)
                .get()
                .await()

            val matchingItem = snapshot.children.firstOrNull {
                it.child("name").value == name
            }

            if (matchingItem != null) {
                val existingQty = matchingItem.child("qty").value.toString().toDoubleOrNull() ?: 0.0

                if (existingQty > qty) {
                    val newQty = existingQty - qty
                    val updatedData = mapOf(
                        "qty" to newQty,
                        "updatedAt" to System.currentTimeMillis()
                    )
                    matchingItem.ref.updateChildren(updatedData).await()
                } else {
                    matchingItem.ref.removeValue().await()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
