package com.example.apporder.room.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "order")
open class Order(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String = "",
    var isSelectChart1:Boolean = false,
    var isSelectChart2:Boolean = false,
    var isSelectChartLong:Boolean = false,
    var isShow:Boolean = true,
    var date: Long = 0L,
    var type :Int = TYPE_DOUBLE_CHAIR
) : Parcelable {

    companion object{
        const val TYPE_LONG_CHAIR = 0
        const val TYPE_DOUBLE_CHAIR = 1
        const val TYPE_DIVIDE = 2
    }

}