package com.example.apporder.room.repository

import com.example.apporder.room.AppDao
import com.example.apporder.room.model.Order

class FileDataRepositoryImpl(val dao: AppDao) : FileDataRepository {
    override fun getAllOrder(): List<Order> {
        return dao.getAllData()
    }

    override fun insertOrder(order: Order): Long {
        val id = dao.insert(order)
        return id
    }

    override fun updateOrder(it: Order) {
        dao.update(it)
    }
}