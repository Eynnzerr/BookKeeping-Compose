package com.eynnzerr.cpbookkeeping_compose.data

import com.eynnzerr.cpbookkeeping_compose.base.CPApplication.Companion.context
import com.eynnzerr.cpbookkeeping_compose.model.DetailData
import com.eynnzerr.cpbookkeeping_compose.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class BillRepositoryImpl @Inject constructor() : BillRepository {
    private val billDao = BillDatabase.getInstance(context).getDao()

    fun getBillsByDate(date: String) = billDao.getBillsByDate(date)

    /**
     * get Flow of billList by month, year, and category.
     */
    fun getBillsByMonthYear(month: Int, year: Int, category: Int) = billDao.getBillsByMonthYear(month, year, category)

    /**
     * get Flow of billList by year, and category.
     */
    fun getBillsByYear(year: Int, category: Int) = billDao.getBillsByYear(year, category)

    /**
     * get Flow of sum of bill amount each day in a given month, which is needed for LineChart.
     */
    fun getSumFlowByDay(month: Int, category: Int) = billDao.getSumByDay(month, category)

    /**
     * get Flow of sum of bill amount each month in a given year, which is needed for LineChart.
     */
    fun getSumFlowByMonth(year: Int, category: Int) = billDao.getSumByMonth(year, category)

    /**
     * get Flow of sum of bill amount each type in a given month, which is needed for PieChart.
     */
    fun getDaySumFlowByType(month: Int, category: Int) = billDao.getDaySumByType(month, category)

    /**
     * get Flow of sum of bill amount each type in a given year, which is needed for PieChart.
     */
    fun getMonthSumFlowByType(year: Int, category: Int) = billDao.getMonthSumByType(year, category)

    /**
     * collect all data that DetailScreen needs one-time and wrap them as DetailData.
     */
    suspend fun getDetailData(month: Int, year: Int, category: Int): DetailData {
        val lineDataMonthly = getSumFlowByDay(month, category).first()
        val lineDataYearly = getSumFlowByMonth(year, category).first()
        val pieDataMonthly = getDaySumFlowByType(month, category).first()
        val pieDataYearly = getMonthSumFlowByType(year, category).first()
        return DetailData(lineDataMonthly, lineDataYearly, pieDataMonthly, pieDataYearly)
    }

    /**
     * get Flow of all the bills in database.
     */
    override suspend fun getBillsFlow(): Flow<List<Bill>> = billDao.getAllBills()

    /**
     * Insert a single bill into database and update dataStore automatically.
     */
    override suspend fun insertBill(bill: Bill) {
        billDao.insertBills(bill)
        val oldBudget = getFloatData(BUDGET, 0f)
        if (bill.category == -1) {
            val day = getIntData(DAY_RECORD, 1)
            val month = getIntData(MONTH_RECORD, 1)
            if (day == bill.day) {
                val oldDaily = getFloatData(DAILY_EXPENSE, 0f)
                updateFloatData(oldDaily + bill.amount, DAILY_EXPENSE)
            }
            if (month == bill.month) {
                val oldMonthly = getFloatData(MONTHLY_EXPENSE,0f)
                updateFloatData(oldMonthly + bill.amount, MONTHLY_EXPENSE)
            }
            updateFloatData(oldBudget - bill.amount, BUDGET)
        }
        else if (bill.category == 1) {
            val day = getIntData(DAY_RECORD, 1)
            val month = getIntData(MONTH_RECORD, 1)
            if (day == bill.day) {
                val oldDaily = getFloatData(DAILY_REVENUE, 0f)
                updateFloatData(oldDaily + bill.amount, DAILY_REVENUE)
            }
            if (month == bill.month) {
                val oldMonthly = getFloatData(MONTHLY_REVENUE,0f)
                updateFloatData(oldMonthly + bill.amount, MONTHLY_REVENUE)
            }
            updateFloatData(oldBudget + bill.amount, BUDGET)
        }
    }

    /**
     * Delete a single bill from database and update dataStore automatically
     */
    override suspend fun deleteBill(bill: Bill) {
        billDao.deleteBills(bill)
        val oldBudget = getFloatData(BUDGET, 0f)
        if (bill.category == -1) {
            val day = getIntData(DAY_RECORD, 1)
            val month = getIntData(MONTH_RECORD, 1)
            if (day == bill.day) {
                val oldDaily = getFloatData(DAILY_EXPENSE, 0f)
                updateFloatData(oldDaily - bill.amount, DAILY_EXPENSE)
            }
            if (month == bill.month) {
                val oldMonthly = getFloatData(MONTHLY_EXPENSE,0f)
                updateFloatData(oldMonthly - bill.amount, MONTHLY_EXPENSE)
            }
            updateFloatData(oldBudget + bill.amount, BUDGET)
        }
        else if (bill.category == 1) {
            val day = getIntData(DAY_RECORD, 1)
            val month = getIntData(MONTH_RECORD, 1)
            if (day == bill.day) {
                val oldDaily = getFloatData(DAILY_REVENUE, 0f)
                updateFloatData(oldDaily - bill.amount, DAILY_REVENUE)
            }
            if (month == bill.month) {
                val oldMonthly = getFloatData(MONTHLY_REVENUE,0f)
                updateFloatData(oldMonthly - bill.amount, MONTHLY_REVENUE)
            }
            updateFloatData(oldBudget - bill.amount, BUDGET)
        }
    }

    /**
     * Edit a single bill
     */
    override suspend fun updateBill(bill: Bill) = billDao.updateBills(bill)

}
