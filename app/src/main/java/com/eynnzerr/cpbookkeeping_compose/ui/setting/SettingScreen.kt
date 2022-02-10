package com.eynnzerr.cpbookkeeping_compose.ui.setting

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

private const val TAG = "SettingScreen"

@Composable
fun SettingScreen(uiState: SettingUiState, add: () -> Unit) {
    Text(
        text = "即将更新...",
        textAlign = TextAlign.Center
    )
}