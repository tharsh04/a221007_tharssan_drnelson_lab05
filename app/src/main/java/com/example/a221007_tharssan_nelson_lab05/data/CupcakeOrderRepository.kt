package com.example.a221007_tharssan_nelson_lab05.data

import kotlinx.coroutines.flow.Flow

class CupcakeOrderRepository(private val cupcakeOrderDao: CupcakeOrderDao) {
    val allOrders: Flow<List<CupcakeOrderEntity>> = cupcakeOrderDao.getAllOrders()

    suspend fun insert(order: CupcakeOrderEntity) {
        cupcakeOrderDao.insert(order)
    }

    suspend fun delete(order: CupcakeOrderEntity) {
        cupcakeOrderDao.delete(order)
    }
}