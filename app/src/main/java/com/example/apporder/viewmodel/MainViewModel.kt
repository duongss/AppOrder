package com.example.apporder.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apporder.extension.getDateTime
import com.example.apporder.room.model.LongTable
import com.example.apporder.room.model.Order
import com.example.apporder.room.repository.FileDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(var orderRepository: FileDataRepository) : ViewModel() {

    val numberChairGrid1 = 12
    val numberChairVip = 3
    val numberChairLong = 2
    val numberDivide = 6

    val numberNonOrder = MutableLiveData<Int>()
    val listData = MutableLiveData<List<Order>>()
    var timeData = System.currentTimeMillis()

    fun initData(time: Long = timeData) = viewModelScope.launch(Dispatchers.IO) {
        timeData = time
        var listDb = orderRepository.getAllOrder().filter {
            it.date.getDateTime() == timeData.getDateTime()
        }

        if (listDb.isEmpty()) {
            for (i in 0 until numberChairGrid1) {
                orderRepository.insertOrder(Order(date = timeData))
            }

            for (i in 0 until numberDivide) {
                if (i == 0 || i == numberDivide - 1) {
                    orderRepository.insertOrder(
                        Order(type = Order.TYPE_DIVIDE, date = timeData)
                    )
                } else {
                    orderRepository.insertOrder(
                        Order(
                            type = Order.TYPE_DIVIDE,
                            isShow = false,
                            date = timeData
                        )
                    )
                }
            }


            for (i in 0 until numberChairLong) {
                val longChairs = arrayListOf(LongTable(), LongTable(), LongTable())
                orderRepository.insertOrder(
                    Order(
                        type = Order.TYPE_LONG_CHAIR,
                        date = timeData,
                        listLongTable = longChairs
                    )
                )
            }

            for (i in 0 until numberChairVip) {
                orderRepository.insertOrder(
                    Order(
                        type = Order.TYPE_DOUBLE_CHAIR_VIP,
                        date = timeData
                    )
                )
            }

            listDb = orderRepository.getAllOrder().filter {
                it.date.getDateTime() == timeData.getDateTime()
            }

        }

        var countOfFalseIsSelect: Int =
            listDb.count { order -> !order.isSelected && order.isShow && order.type != Order.TYPE_DIVIDE && order.type != Order.TYPE_LONG_CHAIR && !order.isSpecial }

        listDb.forEach {
            if (it.isShow && it.type == Order.TYPE_LONG_CHAIR) {
                it.listLongTable.forEach {
                    if (!it.isSelected) {
                        countOfFalseIsSelect += 1
                    }
                }
            }
        }

        numberNonOrder.postValue(countOfFalseIsSelect)

        try {
            var c = 1
            listDb.forEach {
                if (it.isShow && it.type != Order.TYPE_DIVIDE && !it.isSpecial) {
                    if (it.type == Order.TYPE_LONG_CHAIR) {
                        it.listLongTable.forEach {
                            it.stt = c
                            c++
                        }
                    } else {
                        it.stt = c
                        c++
                    }
                }
            }
        } catch (e: Exception) {

        }
        listData.postValue(ArrayList(listDb))
    }

    fun updateOrder(data: Order) = viewModelScope.launch(Dispatchers.IO) {
        orderRepository.updateOrder(data)
        initData()
    }

    fun resetData() = viewModelScope.launch(Dispatchers.IO) {
        orderRepository.deleteAll()
        initData()
    }
}