package com.example.apporder.room.repository

import com.example.apporder.room.model.Order

interface FileDataRepository {
    fun getAllOrder(): List<Order>

    fun insertOrder(it: Order): Long

    fun updateOrder(it: Order)
}