package com.example.a221007_tharssan_nelson_lab05

import android.app.Application
import com.example.a221007_tharssan_nelson_lab05.data.CupcakeOrderDatabase
import com.example.a221007_tharssan_nelson_lab05.data.CupcakeOrderRepository

class CupcakeApplication : Application() {
    // Menggunakan lazy supaya database hanya dibina bila dipanggil pertama kali
    val database: CupcakeOrderDatabase by lazy { CupcakeOrderDatabase.getDatabase(this) }
    val repository: CupcakeOrderRepository by lazy { CupcakeOrderRepository(database.orderDao()) }
}