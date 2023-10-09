package com.example.apporder.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apporder.extension.getDateTime
import com.example.apporder.room.model.Order
import com.example.apporder.room.repository.FileDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(var orderRepository: FileDataRepository) : ViewModel() {

    val numberChairGrid1 = 9
    val numberChairVip = 2
    val numberChairLong = 4
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

            for (i in 0 until numberChairVip) {
                orderRepository.insertOrder(
                    Order(
                        type = Order.TYPE_DOUBLE_CHAIR_VIP,
                        date = timeData
                    )
                )
            }

            for (i in 0 until numberChairLong) {
                orderRepository.insertOrder(Order(type = Order.TYPE_LONG_CHAIR, date = timeData))
            }

            listDb = orderRepository.getAllOrder().filter {
                it.date.getDateTime() == timeData.getDateTime()
            }

        }

        try {
            val countOfFalseIsSelect: Int =
                listDb.count { order -> !order.isSelectChart1 && order.isShow && order.type != Order.TYPE_DIVIDE && order.type != Order.TYPE_ONLY_TABLE && order.type != Order.TYPE_LONG_CHAIR && !order.isSpecial } +
                        listDb.count { order -> !order.isSelectChart2 && order.isShow && order.type != Order.TYPE_DIVIDE && order.type != Order.TYPE_ONLY_TABLE && order.type != Order.TYPE_LONG_CHAIR && !order.isSpecial } +
                        listDb.count { order -> !order.isSelectChartLong && order.isShow && order.type != Order.TYPE_DIVIDE && order.type != Order.TYPE_ONLY_TABLE && order.type != Order.TYPE_DOUBLE_CHAIR && ((order.type == Order.TYPE_DOUBLE_CHAIR_VIP && order.isSpecial) || order.type == Order.TYPE_LONG_CHAIR) }

            numberNonOrder.postValue(countOfFalseIsSelect)

            var c = 1
            listDb.forEach {
                if (it.isShow && it.type != Order.TYPE_DIVIDE && !it.isSpecial) {
                    it.stt = c
                    c++
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