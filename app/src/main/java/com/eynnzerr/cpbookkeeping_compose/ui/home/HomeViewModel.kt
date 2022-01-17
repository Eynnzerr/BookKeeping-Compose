package com.eynnzerr.cpbookkeeping_compose.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.data.BillRepositoryImpl
import com.eynnzerr.cpbookkeeping_compose.model.HomeData
import com.eynnzerr.cpbookkeeping_compose.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
    val uiState: StateFlow<HomeUiState> = _uiState //post to ui. Composables can call collectAsState to collect data in viewmodel.

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(homeData = getAllData(0f)) }
            billRepository.getBillsFlow().collect { bills ->
                _uiState.update { it.copy(billsToday = bills) }
            }
        }
    }

    fun updateBills() {
        viewModelScope.launch {
            _uiState.update { it.copy(homeData = getAllData(0f)) }
            billRepository.getBillsFlow().collect { bills ->
                _uiState.update { it.copy(billsToday = bills) }
            }
        }
    }

    fun deleteBill(bill: Bill) {
        viewModelScope.launch {
            billRepository.deleteBill(bill)
        }
    }
}