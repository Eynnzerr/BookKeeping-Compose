package com.eynnzerr.cpbookkeeping_compose.data

import kotlinx.coroutines.flow.Flow

interface BillRepository {

    suspend fun getBillsFlow(): Flow<List<Bill>>

    suspend fun insertBill(bill: Bill)

    suspend fun deleteBill(bill: Bill)

    suspend fun updateBill(bill: Bill)

}