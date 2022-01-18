package com.eynnzerr.cpbookkeeping_compose.ui.record

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.ui.basic.BillListWithHeader

@ExperimentalFoundationApi
@Composable
fun RecordScreen(
    uiState: RecordUiState,
    listState: LazyListState,
    updateBills: () -> Unit,
    onDeleteBill: (Bill) -> Unit
) {
    updateBills()
    Column {
        Spacer(modifier = Modifier.padding(vertical = 20.dp))
        BillListWithHeader(
            grouped = uiState.groupedBills,
            listState = listState,
            onEdit = {/*TODO*/},
            onDelete = onDeleteBill
        )
    }
}