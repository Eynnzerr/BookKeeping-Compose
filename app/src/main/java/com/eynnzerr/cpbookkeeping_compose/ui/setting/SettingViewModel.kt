package com.eynnzerr.cpbookkeeping_compose.ui.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SettingViewModel"

data class SettingUiState(
    var count: Int,
    var msg: String
)

@HiltViewModel
class SettingViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingUiState(0, "init"))
    val uiState: StateFlow<SettingUiState> = _uiState

    fun add() {
        viewModelScope.launch {
            delay(1000)
            _uiState.update { state -> SettingUiState(state.count + 1, "last:${state.count}") }
            Log.d(TAG, "add: value=${_uiState.value.count}")
        }
    }
}