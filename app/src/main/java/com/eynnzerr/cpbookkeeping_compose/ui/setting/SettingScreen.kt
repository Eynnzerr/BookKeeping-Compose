package com.eynnzerr.cpbookkeeping_compose.ui.setting

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eynnzerr.cpbookkeeping_compose.R

@Composable
fun SettingScreen(uiState: SettingUiState, onFingerOptionChanged: (Boolean) -> Unit) {
    val context = LocalContext.current
    val pos_text = stringResource(id = R.string.positive_toast_text)
    val neg_text = stringResource(id = R.string.negative_toast_text)
    Surface(
        color = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 5.dp)
        ) {
            Text(
                text = stringResource(id = R.string.enable_fingerprint),
                fontSize = 20.sp,
                color = Color.Black
            )
            Switch(
                checked = uiState.fingerEnabled,
                onCheckedChange = {
                    onFingerOptionChanged(it)
                     val text = if (it) pos_text else neg_text
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}