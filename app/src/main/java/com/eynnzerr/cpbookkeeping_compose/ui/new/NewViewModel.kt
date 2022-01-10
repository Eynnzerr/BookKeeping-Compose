package com.eynnzerr.cpbookkeeping_compose.ui.new

import androidx.lifecycle.ViewModel
import com.eynnzerr.cpbookkeeping_compose.data.BillRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//data class NewUiState(
//)

@HiltViewModel
class NewViewModel @Inject constructor(
    private val billRepository: BillRepositoryImpl
): ViewModel() {

}