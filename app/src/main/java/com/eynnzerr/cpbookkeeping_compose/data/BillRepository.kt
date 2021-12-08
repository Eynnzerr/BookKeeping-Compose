package com.eynnzerr.cpbookkeeping_compose.data

import kotlinx.coroutines.flow.Flow

interface BillRepository {

    suspend fun getBillsFlow(): Flow<List<Bill>>

    suspend fun insertBills(vararg bill: Bill)

    suspend fun deleteBills(vararg bill: Bill)

    suspend fun updateBills(vararg bill: Bill)

}