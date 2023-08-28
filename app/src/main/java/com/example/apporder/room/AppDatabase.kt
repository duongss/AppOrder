package com.example.apporder.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.apporder.BuildConfig
import com.example.apporder.room.model.Order


@Database(
    entities = [Order::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): AppDao

    companion object {
        const val DATABASE_NAME = BuildConfig.APPLICATION_ID + "s1.db"
    }

}