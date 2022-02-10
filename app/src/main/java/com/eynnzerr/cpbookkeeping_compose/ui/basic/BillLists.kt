package com.eynnzerr.cpbookkeeping_compose.ui.basic

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.data.billTypes
import com.eynnzerr.cpbookkeeping_compose.R

@Composable
fun BillList(
    bills: List<Bill>,
    listState: LazyListState,
    onEdit: (Bill) -> Unit,
    onDelete: (Bill) -> Unit
) {
    LazyColumn(
        state = listState
    ) {
        items(bills) { bill ->
            BillCard(bill = bill, onEdit = onEdit, onDelete = onDelete)
        }
    }
}

@Composable
fun BillList(
    bills: List<Bill>,
    onEdit: (Bill) -> Unit,
    onDelete: (Bill) -> Unit
) {
    LazyColumn {
        items(bills) { bill ->
            BillCard(bill = bill, onEdit = onEdit, onDelete = onDelete)
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun BillListWithHeader(
    grouped: Map<String, List<Bill>>,
    listState: LazyListState,
    onEdit: (Bill) -> Unit,
    onDelete: (Bill) -> Unit,
    openAnalysis: (Int, Int) -> Unit
) {
    LazyColumn(
        state = listState
    ) {
        grouped.forEach { (groupDate, groupBills) ->
            val month = groupBills[0].month
            val year = groupBills[0].year
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = groupDate,
                        style = MaterialTheme.typography.body1,
                        color = Color.Black
                    )
                    Text(
                        text = stringResource(id = R.string.monthly_bills),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.clickable { openAnalysis(month, year) }
                    )
                }
            }
            items(groupBills) { bill ->
                BillCard(bill = bill, onEdit = onEdit, onDelete = onDelete)
            }
        }
    }
}

@Composable
fun BillCard(bill: Bill, onEdit: (Bill) -> Unit,onDelete: (Bill) -> Unit) {
    val tintColor = if(bill.category == -1) Color.Red else Color.Blue
    var isDialogShown by remember { mutableStateOf(false) }
    if (isDialogShown) {
        Dialog(onDismissRequest = { isDialogShown = false }) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .height(120.dp)
                    .background(color = Color.White)
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    onClick = {
                    onEdit(bill)
                    isDialogShown = false
                }) {
                    Icon(
                        imageVector =  Icons.Filled.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = stringResource(id = R.string.edit),
                        style = MaterialTheme.typography.body2
                    )
                }
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    onClick = {
                    onDelete(bill)
                    isDialogShown = false
                }) {
                    Icon(
                        imageVector =  Icons.Filled.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = stringResource(id = R.string.delete),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
    Surface(
        color = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 3.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {},
                    onLongPress = { isDialogShown = true }
                )
            }
    ) {
        Row(
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = bill.type),
                    contentDescription = null,
                    //TODO 加上选中变色(支出变红，收入变蓝)，常态灰白色的效果。且裁剪为圆形，有背景色和图标色
                    tint = tintColor
                )
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Text(
                    text = billTypes[bill.type]!!,
                    style = MaterialTheme.typography.body2
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "¥" + String.format("%.2f", bill.amount),
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                Text(
                    text = bill.date,
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.End,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Preview(
    name = "BalanceSurface",
    showBackground = true
)
@Composable
private fun PreviewBillCard() {
    val bill = Bill(
        type = R.drawable.type_other,
        category = -1,
        date = "2022-01-31"
    )
    BillCard(bill = bill, onEdit = {}, onDelete = {})
}