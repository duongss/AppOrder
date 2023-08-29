package com.example.apporder.room

import androidx.room.*
import com.example.apporder.room.model.Order

@Dao
interface AppDao {
    @Query("SELECT * FROM `order`")
    fun getAllData(): List<Order>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(order: Order): Long

    @Delete
    fun delete(order: Order)

    @Update
    fun update(order: Order)

    @Query("DELETE FROM `order`")
    fun deleteAllData()
}