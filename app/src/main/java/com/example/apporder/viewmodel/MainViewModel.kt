package com.example.apporder.viewmodel

import android.util.Log
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
    val numberChairGrid2 = 3
    val numberDivide = 6
    val numberChairLong = 2

    val numberNonOrder = MutableLiveData<Int>()
    val listData = MutableLiveData<List<Order>>()

    fun initData() = viewModelScope.launch(Dispatchers.IO) {
        var listDb = orderRepository.getAllOrder()

        if (listDb.isEmpty()) {
            for (i in 0 until numberChairGrid1) {
                orderRepository.insertOrder(Order(date = System.currentTimeMillis()))
            }

            for (i in 0 until numberDivide) {
                if (i == 0 || i == numberDivide - 1) {
                    orderRepository.insertOrder(
                        Order(
                            date = System.currentTimeMillis(),
                            type = Order.TYPE_DIVIDE
                        )
                    )
                } else {
                    orderRepository.insertOrder(
                        Order(
                            date = System.currentTimeMillis(),
                            type = Order.TYPE_DIVIDE,
                            isShow = false
                        )
                    )
                }
            }

            for (i in 0 until numberChairGrid2) {
                if (i == 0) {
                    orderRepository.insertOrder(
                        Order(
                            date = System.currentTimeMillis(),
                            isShow = false
                        )
                    )
                } else {
                    orderRepository.insertOrder(Order(date = System.currentTimeMillis()))
                }
            }

            for (i in 0 until numberChairLong) {
                orderRepository.insertOrder(
                    Order(
                        date = System.currentTimeMillis(),
                        type = Order.TYPE_LONG_CHAIR
                    )
                )
            }
            listDb = orderRepository.getAllOrder()

        }
        Log.d("hahahaha", "initData: ${listDb.size}")

        val countOfFalseIsSelect: Int =
            listDb.count { order -> !order.isSelectChart1  && order.type != Order.TYPE_DIVIDE && order.type != Order.TYPE_LONG_CHAIR && order.isShow } +
                    listDb.count { order -> !order.isSelectChart2 && order.isShow && order.type != Order.TYPE_DIVIDE && order.type != Order.TYPE_LONG_CHAIR }+
                    listDb.count { order -> !order.isSelectChartLong && order.isShow && order.type != Order.TYPE_DIVIDE && order.type != Order.TYPE_DOUBLE_CHAIR }

        numberNonOrder.postValue(countOfFalseIsSelect)
        listData.postValue(listDb)
    }

    fun updateOrder(data: Order) = viewModelScope.launch(Dispatchers.IO) {
        orderRepository.updateOrder(data)
        initData()
    }
}