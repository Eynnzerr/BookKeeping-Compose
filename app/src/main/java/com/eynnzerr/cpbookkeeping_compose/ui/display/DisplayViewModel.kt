package com.eynnzerr.cpbookkeeping_compose.ui.display

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.data.BillRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisplayViewModel @Inject constructor(
    private val billRepository: BillRepositoryImpl
): ViewModel() {
    val billState = MutableStateFlow(Bill())

    fun loadBillById(id: Int) {
        viewModelScope.launch {
            billState.update { billRepository.getBillById(id) }
        }
    }
}