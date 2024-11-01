package com.example.smartshop

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.smartshop.db.Entities.Shoppinglist
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    fun insertShoppinglist(list: Shoppinglist)

    @Query("SELECT * FROM shoppinglists")
    fun getAllShoppinglists(): Flow<List<Shoppinglist>>
}