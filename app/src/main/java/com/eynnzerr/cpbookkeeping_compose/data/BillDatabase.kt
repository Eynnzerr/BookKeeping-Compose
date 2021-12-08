package com.eynnzerr.cpbookkeeping_compose.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, exportSchema = false, entities = [Bill::class])
abstract class BillDatabase: RoomDatabase() {
    abstract fun getDao(): BillDao
    //using companion to achieve singleton
    companion object {
        private const val databaseName = "cp-bookkeeping"
        private var INSTANCE: BillDatabase? = null
        @Synchronized
        fun getInstance(context: Context): BillDatabase {
            return INSTANCE?: Room.databaseBuilder(context.applicationContext, BillDatabase::class.java, databaseName).build()
        }
    }
}