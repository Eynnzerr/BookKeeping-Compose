package com.eynnzerr.cpbookkeeping_compose.data

import com.eynnzerr.cpbookkeeping_compose.base.CPApplication.Companion.context
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BillRepositoryImpl @Inject constructor() : BillRepository {
    private val billDao = BillDatabase.getInstance(context).getDao()

    override suspend fun getBillsFlow(): Flow<List<Bill>> = billDao.getAllBills()

    override suspend fun insertBills(vararg bill: Bill) = billDao.insertBills(*bill)

    override suspend fun deleteBills(vararg bill: Bill) = billDao.deleteBills(*bill)

    override suspend fun updateBills(vararg bill: Bill) = billDao.updateBills(*bill)
}