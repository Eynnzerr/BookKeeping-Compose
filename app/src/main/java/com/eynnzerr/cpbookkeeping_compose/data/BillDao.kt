package com.eynnzerr.cpbookkeeping_compose.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {
    @Query("SELECT * FROM bills")
    fun getAllBills(): Flow<List<Bill>>

    @Query("SELECT * FROM bills WHERE category = -1")
    suspend fun getExpenses(): List<Bill>

    @Query("SELECT * FROM bills WHERE category = 1")
    suspend fun getRevenues(): List<Bill>

    @Insert
    suspend fun insertBills(vararg bill: Bill)

    @Delete
    suspend fun deleteBills(vararg bill: Bill)

    @Update
    suspend fun updateBills(vararg bill: Bill)
}