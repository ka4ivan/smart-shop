package com.smartshop.data.repository

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.smartshop.data.model.ListData
import kotlinx.coroutines.tasks.await

class ListRepository {
    private val database = FirebaseDatabase.getInstance("https://lntu-94305-default-rtdb.europe-west1.firebasedatabase.app").reference.child("lists")

    suspend fun getListsOnce(userId: String): List<ListData> {
        return try {
            val snapshot = database
                .orderByChild("userId")
                .equalTo(userId)
                .get()
                .await()

            val lists = snapshot.children.mapNotNull {
                val listData = it.getValue(ListData::class.java)
                listData
            }

            lists
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getLists(userId: String): List<ListData> {
        return try {
            Log.d("ListRepository", "Fetching lists for userId: $userId")

            val snapshot = database
                .orderByChild("userId")
                .equalTo(userId)
                .get()
                .await()

            val lists = snapshot.children.mapNotNull {
                val listData = it.getValue(ListData::class.java)
                listData
            }

            lists
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun createList(list: ListData) {
        val timestamp = System.currentTimeMillis()
        val listWithTimestamp = list.copy(createdAt = timestamp, updatedAt = timestamp)

        val newListRef = database.push()
        val newListWithId = listWithTimestamp.copy(id = newListRef.key ?: "")

        newListRef.setValue(newListWithId).await()
    }


    suspend fun readList(listId: String): ListData? {
        val snapshot = database.child(listId).get().await()
        return snapshot.getValue(ListData::class.java)
    }

    suspend fun updateList(listId: String, updatedList: ListData) {
        database.child(listId).setValue(updatedList).await()
    }

    suspend fun deleteList(listId: String) {
        database.child(listId).removeValue().await()
    }

    suspend fun getAllLists(): List<ListData> {
        val snapshot = database.get().await()
        return snapshot.children.mapNotNull { it.getValue(ListData::class.java) }
    }
}
