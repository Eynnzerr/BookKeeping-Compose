package com.eynnzerr.cpbookkeeping_compose.ui.detail

import androidx.lifecycle.ViewModel
import com.eynnzerr.cpbookkeeping_compose.data.BillRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: BillRepositoryImpl
): ViewModel() {

}