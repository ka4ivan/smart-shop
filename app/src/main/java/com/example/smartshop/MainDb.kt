package com.example.smartshop

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smartshop.db.Entities.Shoppinglist

@Database (entities = [Shoppinglist::class], version = 1)
abstract class MainDb : RoomDatabase() {
    abstract fun getDao(): Dao

    companion object {
        fun getDb(context: Context): MainDb {
            return Room.databaseBuilder(context.applicationContext, MainDb::class.java, "smart-shop").build()
        }
    }
}