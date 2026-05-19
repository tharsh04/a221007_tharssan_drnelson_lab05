package com.example.a221007_tharssan_nelson_lab05.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CupcakeOrderDao {
    @Insert
    suspend fun insert(order: CupcakeOrderEntity)

    @Query("SELECT * FROM orders ORDER BY id DESC")
    fun getAllOrders(): Flow<List<CupcakeOrderEntity>>

    @Delete
    suspend fun delete(order: CupcakeOrderEntity)
}