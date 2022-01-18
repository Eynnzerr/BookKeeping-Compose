package com.eynnzerr.cpbookkeeping_compose.ui.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.data.BillRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecordUiState(
    val groupedBills: Map<String, List<Bill>> = emptyMap()
)

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val billRepository: BillRepositoryImpl
): ViewModel() {
    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState = _uiState.asStateFlow()

    init {
        updateBills()
    }

    fun updateBills() {
        viewModelScope.launch {
            billRepository.getBillsFlow().collect { bills ->
                val grouped = bills.groupBy { bill -> bill.date.substring(0,7) }
                _uiState.update { it.copy(groupedBills = grouped) }
            }
        }
    }

    fun deleteBill(bill: Bill) {
        viewModelScope.launch {
            billRepository.deleteBill(bill)
        }
    }
}