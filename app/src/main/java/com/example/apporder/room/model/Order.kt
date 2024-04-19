package com.example.apporder.room.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "order")
open class Order(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String = "",
    var isSelected: Boolean = false,
    var isShow: Boolean = true,
    var date: Long = System.currentTimeMillis(),
    var type: Int = TYPE_DOUBLE_CHAIR,
    var isSpecial: Boolean = false,
    @TypeConverters(ConvertersLongTable::class) var listLongTable: ArrayList<LongTable> = arrayListOf(), // of TYPE_LONG_CHAIR

    @Ignore var stt: Int = 0,
) : Parcelable {

    companion object{
        const val TYPE_LONG_CHAIR = 0
        const val TYPE_DOUBLE_CHAIR = 1
        const val TYPE_DIVIDE = 2
        const val TYPE_DOUBLE_CHAIR_VIP = 3
        const val TYPE_ONLY_TABLE = 4
    }

}