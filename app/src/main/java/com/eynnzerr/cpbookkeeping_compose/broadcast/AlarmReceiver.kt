package com.eynnzerr.cpbookkeeping_compose.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.eynnzerr.cpbookkeeping_compose.utils.DAILY_EXPENSE
import com.eynnzerr.cpbookkeeping_compose.utils.DAILY_REVENUE
import com.eynnzerr.cpbookkeeping_compose.utils.updateFloatData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "time change received.info:${intent.action}")
        //update datastore here
        CoroutineScope(Dispatchers.IO).launch {
            updateFloatData(0f, DAILY_EXPENSE)
            updateFloatData(0f, DAILY_REVENUE)
        }
    }
}