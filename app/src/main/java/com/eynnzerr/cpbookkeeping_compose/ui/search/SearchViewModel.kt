package com.eynnzerr.cpbookkeeping_compose.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.data.BillRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val billRepository: BillRepositoryImpl
): ViewModel() {
    private val _uiState = MutableStateFlow(emptyList<Bill>())
    val uiState: StateFlow<List<Bill>> = _uiState

    fun loadSearchResult(keyWord: String) {
        viewModelScope.launch {

        }
    }
}