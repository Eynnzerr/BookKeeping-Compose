package com.eynnzerr.cpbookkeeping_compose.ui.detail

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.data.BillRepositoryImpl
import com.eynnzerr.cpbookkeeping_compose.data.BillStatistic
import com.eynnzerr.cpbookkeeping_compose.model.DetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

private const val TAG = "DetailViewModel"

data class DetailUiState constructor(
    // 对于lineData，先确定当前月份有多少天，再创建天数长度的列表且初始值全为0，再对这个列表，依次填入当天对应的金额。
    val detailData: DetailData = DetailData(),
    val bills: List<Bill> = emptyList(),
    val dateToday: String = ""
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: BillRepositoryImpl
): ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState
    var month = 0
    var year = 0

    fun loadData(category: Int) {
        viewModelScope.launch {
            Log.d("DetailViewModel", "loadData: date: $year-$month")
            val detailData = repository.getDetailData(month, year, category)
            val bills = repository.getBillsByMonthYear(month, year, category).first()
            _uiState.update { DetailUiState(
                detailData = detailData,
                bills = bills,
                dateToday = "${year}年${month}月账单"
                )
            }
        }
    }
}