package com.example.a221007_tharssan_nelson_lab05.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CupcakeOrderEntity::class], version = 1, exportSchema = false)
abstract class CupcakeOrderDatabase : RoomDatabase() {

    abstract fun orderDao(): CupcakeOrderDao

    companion object {
        @Volatile
        private var Instance: CupcakeOrderDatabase? = null

        fun getDatabase(context: Context): CupcakeOrderDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CupcakeOrderDatabase::class.java, "order_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}