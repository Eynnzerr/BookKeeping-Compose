package com.eynnzerr.cpbookkeeping_compose.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.data.BillRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val billRepository: BillRepositoryImpl
): ViewModel() {

    lateinit var billsFlow: Flow<List<Bill>>

    init {
        viewModelScope.launch {
            billsFlow = billRepository.getBillsFlow()
        }
    }
}