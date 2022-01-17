package com.eynnzerr.cpbookkeeping_compose.data

import com.eynnzerr.cpbookkeeping_compose.base.CPApplication.Companion.context
import com.eynnzerr.cpbookkeeping_compose.utils.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BillRepositoryImpl @Inject constructor() : BillRepository {
    private val billDao = BillDatabase.getInstance(context).getDao()

    override suspend fun getBillsFlow(): Flow<List<Bill>> = billDao.getAllBills()

    /*
     * Insert a single bill into database and update dataStore automatically.
     */
    override suspend fun insertBill(bill: Bill) {
        billDao.insertBills(bill)
        val oldBudget = getFloatData(BUDGET, 0f)
        if (bill.category == -1) {
            val oldMonthly = getFloatData(MONTHLY_EXPENSE,0f)
            val oldDaily = getFloatData(DAILY_EXPENSE, 0f)
            updateFloatData(oldMonthly + bill.amount, MONTHLY_EXPENSE)
            updateFloatData(oldDaily + bill.amount, DAILY_EXPENSE)
            updateFloatData(oldBudget - bill.amount, BUDGET)
        }
        else if (bill.category == 1) {
            val oldMonthly = getFloatData(MONTHLY_REVENUE, 0f)
            val oldDaily = getFloatData(DAILY_REVENUE, 0f)
            updateFloatData(oldMonthly - bill.amount, MONTHLY_REVENUE)
            updateFloatData(oldDaily - bill.amount, DAILY_REVENUE)
            updateFloatData(oldBudget + bill.amount, BUDGET)
        }
    }

    /*
     * Delete a single bill from database and update dataStore automatically
     */
    override suspend fun deleteBill(bill: Bill) {
        billDao.deleteBills(bill)
        val oldBudget = getFloatData(BUDGET, 0f)
        if (bill.category == -1) {
            val oldMonthly = getFloatData(MONTHLY_EXPENSE,0f)
            val oldDaily = getFloatData(DAILY_EXPENSE, 0f)
            updateFloatData(oldMonthly - bill.amount, MONTHLY_EXPENSE)
            updateFloatData(oldDaily - bill.amount, DAILY_EXPENSE)
            updateFloatData(oldBudget + bill.amount, BUDGET)
        }
        else if (bill.category == 1) {
            val oldMonthly = getFloatData(MONTHLY_REVENUE, 0f)
            val oldDaily = getFloatData(DAILY_REVENUE, 0f)
            updateFloatData(oldMonthly + bill.amount, MONTHLY_REVENUE)
            updateFloatData(oldDaily + bill.amount, DAILY_REVENUE)
            updateFloatData(oldBudget - bill.amount, BUDGET)
        }
    }

    override suspend fun updateBill(bill: Bill) = billDao.updateBills(bill)
}
