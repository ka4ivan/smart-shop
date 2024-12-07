package com.smartshop.data.repository

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.smartshop.data.model.ListData
import com.smartshop.data.model.ListitemData
import kotlinx.coroutines.tasks.await

class ListRepository {
    private val database = FirebaseDatabase.getInstance("https://lntu-94305-default-rtdb.europe-west1.firebasedatabase.app").reference.child("lists")
    private val databaseListitems = FirebaseDatabase.getInstance("https://lntu-94305-default-rtdb.europe-west1.firebasedatabase.app").reference.child("listitems")

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

    suspend fun getDeletedListsOnce(userId: String): List<ListData> {
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

            lists.filterNot { it.delete == false }
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

    suspend fun createList(list: ListData): ListData {
        val timestamp = System.currentTimeMillis()
        val listWithTimestamp = list.copy(createdAt = timestamp, updatedAt = timestamp)

        val newListRef = database.push()
        val newListWithId = listWithTimestamp.copy(id = newListRef.key ?: "")

        newListRef.setValue(newListWithId).await()
        return newListWithId
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

    suspend fun updateListitem(listitemId: String, updatedListitem: ListitemData) {
        databaseListitems.child(listitemId).setValue(updatedListitem).await()
    }

    suspend fun forceDeleteList(listId: String) {
        database.child(listId).removeValue().await()
    }

    suspend fun deleteList(listId: String) {
        database.child(listId).child("delete").setValue(true).await()
    }

    suspend fun deleteListitem(listitemId: String) {
        databaseListitems.child(listitemId).child("delete").setValue(true).await()
    }

    suspend fun undoList(listId: String) {
        database.child(listId).child("delete").setValue(false).await()
    }

    suspend fun undoListitem(listitemId: String) {
        databaseListitems.child(listitemId).child("delete").setValue(false).await()
    }

    suspend fun getAllLists(): List<ListData> {
        val snapshot = database.get().await()
        return snapshot.children.mapNotNull { it.getValue(ListData::class.java) }
    }

    suspend fun getListitems(listId: String): List<ListitemData> {
        return try {
            val snapshot = databaseListitems
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

    suspend fun uncheckListitems(listId: String) {
        val snapshot = databaseListitems
            .orderByChild("listId")
            .equalTo(listId)
            .get()
            .await()

        if (snapshot.exists()) {
            for (child in snapshot.children) {
                val key = child.key
                if (key != null) {
                    databaseListitems.child(key).child("check").setValue(false).await()
                }
            }
        }
    }

    suspend fun deleteCheckedListitems(listId: String) {
        val snapshot = databaseListitems
            .orderByChild("listId")
            .equalTo(listId)
            .get()
            .await()

        if (snapshot.exists()) {
            for (child in snapshot.children) {
                val listItem = child.getValue(ListitemData::class.java)
                val key = child.key

                if (listItem?.isCheck == true && key != null) {
                    databaseListitems.child(key).removeValue().await()
                }
            }
        }
    }
}
