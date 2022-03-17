package com.eynnzerr.cpbookkeeping_compose.ui.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eynnzerr.cpbookkeeping_compose.utils.NEED_FINGERPRINT
import com.eynnzerr.cpbookkeeping_compose.utils.getBoolData
import com.eynnzerr.cpbookkeeping_compose.utils.updateBoolData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SettingViewModel"

data class SettingUiState(
    val fingerEnabled: Boolean
)

@HiltViewModel
class SettingViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingUiState(false))
    val uiState: StateFlow<SettingUiState> = _uiState

    // initialize fingerPrint option through dataStore.
    init {
        viewModelScope.launch {
            val needFingerPrint = getBoolData(NEED_FINGERPRINT, false)
            _uiState.update { SettingUiState(needFingerPrint) }
        }
    }

    fun modifyFingerOption(enableFinger: Boolean) {
        Log.d(TAG, "modifyFingerOption: $enableFinger")
        viewModelScope.launch {
            updateBoolData(enableFinger, NEED_FINGERPRINT)
            _uiState.update { SettingUiState(enableFinger) }
        }
    }
}