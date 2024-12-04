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

            lists.filterNot { it.delete == true }
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

            lists.filterNot { it.delete == true }
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

    suspend fun showList(listId: String): ListData {
        return try {
            val snapshot = database.child(listId).get().await()
            snapshot.getValue(ListData::class.java) ?: ListData("", "", "", false, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
            ListData("", "", "", false, null, null)
        }
    }

    suspend fun updateList(listId: String, updatedList: ListData) {
        database.child(listId).setValue(updatedList).await()
    }

    suspend fun forceDeleteList(listId: String) {
        database.child(listId).removeValue().await()
    }

    suspend fun deleteList(listId: String) {
        database.child(listId).child("delete").setValue(true).await()
    }

    suspend fun undoList(listId: String) {
        database.child(listId).child("delete").setValue(false).await()
    }

    suspend fun getAllLists(): List<ListData> {
        val snapshot = database.get().await()
        return snapshot.children.mapNotNull { it.getValue(ListData::class.java) }
    }
}
