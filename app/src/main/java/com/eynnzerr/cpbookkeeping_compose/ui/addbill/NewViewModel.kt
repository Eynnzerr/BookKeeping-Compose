package com.eynnzerr.cpbookkeeping_compose.ui.addbill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.data.BillRepositoryImpl
import com.eynnzerr.cpbookkeeping_compose.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TabState(
    val amount: String = "¥0",
    val selectedIndex: Int = 0
)

@HiltViewModel
class NewViewModel @Inject constructor(
    private val billRepository: BillRepositoryImpl
) : ViewModel() {

    //uiState for expenseTab
    private val _exTabState = MutableStateFlow(TabState())
    val exTabState: StateFlow<TabState> = _exTabState.asStateFlow()

    //uiState for revenueTab
    private val _reTabState = MutableStateFlow(TabState())
    val reTabState: StateFlow<TabState> = _reTabState.asStateFlow()

    private val _remarkState = MutableStateFlow("添加备注")
    val remarkState: StateFlow<String> = _remarkState.asStateFlow()

    fun updateRemark(remark: String) {
        _remarkState.update { remark }
    }

    fun updateTab(category: Int, amount: String, selectedIndex: Int) {
        when(category) {
            -1 -> _exTabState.update { it.copy(amount = amount, selectedIndex = selectedIndex) }
            1 -> _reTabState.update { it.copy(amount = amount, selectedIndex = selectedIndex) }
        }
    }

    fun insertBill(bill: Bill) {
        viewModelScope.launch {
            billRepository.insertBill(bill)
        }
    }
}