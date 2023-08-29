package com.example.apporder.room.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "order")
open class Order(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String = "",
    var isSelectChart1: Boolean = false,
    var isSelectChart2: Boolean = false,
    var isSelectChartLong: Boolean = false,
    var isShow: Boolean = true,
    var date: Long = System.currentTimeMillis(),
    var type: Int = TYPE_DOUBLE_CHAIR,
    var isSpecial: Boolean = false,
    @Ignore var stt: Int = 0
) : Parcelable {

    companion object{
        const val TYPE_LONG_CHAIR = 0
        const val TYPE_DOUBLE_CHAIR = 1
        const val TYPE_DIVIDE = 2
        const val TYPE_DOUBLE_CHAIR_VIP = 3
    }

}