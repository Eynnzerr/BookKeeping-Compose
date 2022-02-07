package com.eynnzerr.cpbookkeeping_compose.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eynnzerr.cpbookkeeping_compose.data.BillRepositoryImpl
import com.eynnzerr.cpbookkeeping_compose.data.BillStatistic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "DetailViewModel"

data class DetailUiState(
    val lineDataMonthly: List<BillStatistic> = emptyList(),
    val lineDataYearly: List<BillStatistic> = emptyList(),
    val pieDataMonthly: List<BillStatistic> = emptyList(),
    val pieDataYearly: List<BillStatistic> = emptyList()
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: BillRepositoryImpl
): ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState
    var month = 0
    var year = 0

    fun loadData() {
        viewModelScope.launch {
            Log.d("DetailViewModel", "loadData: date: $year-$month")
/*            repository.getSumFlowByDay(month, -1).collect { data ->
                _uiState.update { it.copy(lineDataMonthly = data) }
            }
            repository.getSumFlowByMonth(year, -1).collect { data ->
                _uiState.update { it.copy(lineDataYearly = data) }
            }*/
            repository.getDaySumFlowByType(month, -1).collect { data ->
                _uiState.update { it.copy(pieDataMonthly = data) }
            }
/*            repository.getMonthSumFlowByType(year, -1).collect { data ->
                _uiState.update { it.copy(pieDataYearly = data) }
            }*/
        }
    }
}