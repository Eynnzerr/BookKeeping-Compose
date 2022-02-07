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

    @Query("SELECT day AS mIndex, SUM(amount) AS mValue FROM bills WHERE month = :month AND category = :category GROUP BY day")
    fun getSumByDay(month: Int, category: Int): Flow<List<BillStatistic>>

    @Query("SELECT month AS mIndex, SUM(amount) AS mValue FROM bills WHERE year = :year AND category = :category GROUP BY month")
    fun getSumByMonth(year: Int, category: Int): Flow<List<BillStatistic>>

    @Query("SELECT type AS mIndex, SUM(amount) AS mValue FROM bills WHERE month = :month AND category = :category GROUP BY type")
    fun getDaySumByType(month: Int, category: Int): Flow<List<BillStatistic>>

    @Query("SELECT type AS mIndex, SUM(amount) AS mValue FROM bills WHERE year = :year AND category = :category GROUP BY type")
    fun getMonthSumByType(year: Int, category: Int): Flow<List<BillStatistic>>
}