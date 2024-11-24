package com.example.myfirstapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDao {
    @Insert
    suspend fun insertItem(item: Item)

    @Query("SELECT * FROM items")
    suspend fun getAllItems(): List<Item>
}
