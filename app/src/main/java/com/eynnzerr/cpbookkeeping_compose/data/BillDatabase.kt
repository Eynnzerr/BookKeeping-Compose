package com.eynnzerr.cpbookkeeping_compose.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(version = 2, exportSchema = false, entities = [Bill::class])
abstract class BillDatabase: RoomDatabase() {
    abstract fun getDao(): BillDao
    //using companion to achieve singleton
    companion object {
        private const val databaseName = "cp-bookkeeping"
        private var INSTANCE: BillDatabase? = null
        private val MIGRATION_1_2 = Migration(1,2) {
            it.execSQL("ALTER TABLE bills ADD COLUMN day INTEGER NOT NULL DEFAULT 0")
            it.execSQL("ALTER TABLE bills ADD COLUMN month INTEGER NOT NULL DEFAULT 0")
            it.execSQL("ALTER TABLE bills ADD COLUMN year INTEGER NOT NULL DEFAULT 0")
        }
        @Synchronized
        fun getInstance(context: Context): BillDatabase {
            return INSTANCE?: Room.databaseBuilder(context.applicationContext, BillDatabase::class.java, databaseName)
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}