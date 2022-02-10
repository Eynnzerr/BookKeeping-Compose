package com.eynnzerr.cpbookkeeping_compose.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.updateTransition
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.data.BillRepositoryImpl
import com.eynnzerr.cpbookkeeping_compose.model.HomeData
import com.eynnzerr.cpbookkeeping_compose.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

data class HomeUiState(
    val billsToday: List<Bill> = emptyList(),
    val homeData: HomeData = HomeData()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val billRepository: BillRepositoryImpl
): ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState
    var currentMonth = 0
    var currentYear = 0

    @SuppressLint("NewApi")
    fun loadData() {
        viewModelScope.launch {
            // check if data needs updating.
            val oldDay = getIntData(DAY_RECORD, 0)
            val oldMonth = getIntData(MONTH_RECORD, 0)
            val calendar = Calendar.getInstance()
            val today = calendar.get(Calendar.DATE)
            val month = calendar.get(Calendar.MONTH) + 1
            currentMonth = month
            currentYear = calendar.get(Calendar.YEAR)
            Log.d("HomeViewModel", "updateBills: old:$oldMonth-$oldDay new:$month-$today")
            if (oldDay != today) {
                updateFloatData(0f, DAILY_EXPENSE)
                updateFloatData(0f, DAILY_REVENUE)
                updateIntData(today, DAY_RECORD)
            }
            if (oldMonth != month) {
                updateFloatData(0f, MONTHLY_EXPENSE)
                updateFloatData(0f, MONTHLY_REVENUE)
                updateIntData(month, MONTH_RECORD)
            }
            val bills = billRepository.getBillsByDate(LocalDate.now().toString()).first()
            _uiState.update { HomeUiState(homeData = getAllData(0f), billsToday = bills) }
        }
    }

    fun deleteBill(bill: Bill) {
        viewModelScope.launch {
            billRepository.deleteBill(bill)
        }
    }
}