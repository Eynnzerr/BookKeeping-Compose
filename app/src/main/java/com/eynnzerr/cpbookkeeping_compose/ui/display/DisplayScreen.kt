package com.eynnzerr.cpbookkeeping_compose.ui.display

import androidx.compose.runtime.Composable
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.ui.basic.ReadOnlyBillCard

@Composable
fun DisplayScreen(bill: Bill) {
    ReadOnlyBillCard(bill = bill)
}