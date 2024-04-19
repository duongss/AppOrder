package com.example.apporder.room.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "longTable")
open class LongTable(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var isSelected: Boolean = false,
    @Ignore var stt: Int = 0
) : Parcelable {

    companion object {
        const val TYPE_LONG_CHAIR = 0
        const val TYPE_DOUBLE_CHAIR = 1
        const val TYPE_DIVIDE = 2
        const val TYPE_DOUBLE_CHAIR_VIP = 3
        const val TYPE_ONLY_TABLE = 4
    }

}

class ConvertersLongTable {
    @TypeConverter
    fun fromArrayList(value: ArrayList<LongTable>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toArrayList(value: String): ArrayList<LongTable> {
        val listType = object : TypeToken<ArrayList<LongTable>>() {}.type
        return Gson().fromJson(value, listType)
    }
}