package com.example.apporder.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apporder.room.model.Order
import com.example.apporder.room.repository.FileDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(var orderRepository: FileDataRepository) : ViewModel() {

    val numberChairGrid1 = 9
    val numberChairGrid2 = 4
    val numberDivide = 6
    val numberChairLong = 2
    val numberChairGrid3 = 4

    val numberNonOrder = MutableLiveData<Int>()
    val listData = MutableLiveData<List<Order>>()

    fun initData() = viewModelScope.launch(Dispatchers.IO) {
        var listDb = orderRepository.getAllOrder()

        if (listDb.isEmpty()) {
            for (i in 0 until numberChairGrid1) {
                orderRepository.insertOrder(Order())
            }

            for (i in 0 until numberDivide) {
                if (i == 0 || i == numberDivide - 1) {
                    orderRepository.insertOrder(
                        Order(type = Order.TYPE_DIVIDE)
                    )
                } else {
                    orderRepository.insertOrder(
                        Order(
                            type = Order.TYPE_DIVIDE,
                            isShow = false
                        )
                    )
                }
            }

            for (i in 0 until numberChairGrid2) {
                if (i == 0) {
                    orderRepository.insertOrder(
                        Order(isShow = false, type = Order.TYPE_DOUBLE_CHAIR_VIP)
                    )
                } else if (i == numberChairGrid2 - 1) {
                    orderRepository.insertOrder(
                        Order(
                            type = Order.TYPE_DOUBLE_CHAIR_VIP,
                            isSpecial = true
                        )
                    )
                } else {
                    orderRepository.insertOrder(Order(type = Order.TYPE_DOUBLE_CHAIR_VIP))
                }
            }

            for (i in 0 until numberChairGrid3) {
                if (i == numberChairGrid2 - 1) {
                    orderRepository.insertOrder(
                        Order(
                            type = Order.TYPE_DOUBLE_CHAIR_VIP,
                            isSpecial = true
                        )
                    )
                } else {
                    orderRepository.insertOrder(Order(type = Order.TYPE_ONLY_TABLE))
                }
            }

            for (i in 0 until numberChairLong) {
                orderRepository.insertOrder(
                    Order(type = Order.TYPE_LONG_CHAIR)
                )
            }
            listDb = orderRepository.getAllOrder()

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
        listData.postValue(listDb)
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