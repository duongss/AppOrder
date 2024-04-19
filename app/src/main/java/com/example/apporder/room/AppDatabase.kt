package com.example.apporder.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.apporder.BuildConfig
import com.example.apporder.room.model.ConvertersLongTable
import com.example.apporder.room.model.LongTable
import com.example.apporder.room.model.Order


@Database(
    entities = [Order::class, LongTable::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(ConvertersLongTable::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): AppDao

    companion object {
        const val DATABASE_NAME = BuildConfig.APPLICATION_ID + "s1.db"
    }

}