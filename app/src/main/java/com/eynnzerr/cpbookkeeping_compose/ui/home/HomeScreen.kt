package com.eynnzerr.cpbookkeeping_compose.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.eynnzerr.cpbookkeeping_compose.R
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.ui.basic.BillList
import com.eynnzerr.cpbookkeeping_compose.utils.setBudget
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    listState: LazyListState,
    openAnalysis: () -> Unit,
    loadData: () -> Unit,
    onDeleteBill: (Bill) -> Unit,
    openDisplay: (Bill) -> Unit
) {
    loadData()
    Column() {
        BalanceSurface(
            expenses = uiState.homeData.monthlyExpense,
            revenue = uiState.homeData.monthlyRevenue,
            budget = uiState.homeData.budget,
            openAnalysis = openAnalysis,
            loadData = loadData
        )
        Spacer(modifier = Modifier.padding(20.dp))
        BalanceToday(
            expenses_today = uiState.homeData.dailyExpense,
            revenue_today = uiState.homeData.dailyRevenue
        )
        BillList(
            bills = uiState.billsToday,
            listState = listState,
            onEdit = {/*TODO 转到编辑页面，可以复用TabContent*/},
            onDelete = { bill -> onDeleteBill(bill) },
            openDisplay = openDisplay
        )
    }
}

@Composable
fun BalanceSurface(
    expenses: Float,
    revenue: Float,
    budget: Float,
    openAnalysis: () -> Unit,
    loadData: () -> Unit
) {
    var eyeClosed by remember{ mutableStateOf(false) }
    var expanded by remember{ mutableStateOf(true) }
    var isDialogShown by remember { mutableStateOf(false) }
    var preBudget by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    if (isDialogShown) {
        Dialog(onDismissRequest = { isDialogShown = false }) {
            Surface(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(8.dp),
                elevation = 5.dp,
                modifier = Modifier
                    .width(300.dp)
                    .padding(horizontal = 5.dp, vertical = 3.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.set_budget),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                    )
                    TextField(
                        value = preBudget,
                        onValueChange = { preBudget = it },
                        leadingIcon = {Icon(painter = painterResource(id = R.drawable.baseline_currency_yuan_24), contentDescription = "")},
                        placeholder = {Text(text = stringResource(id = R.string.input_budget))},
                        modifier = Modifier.padding(5.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent
                        ),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,imeAction = ImeAction.Done)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                preBudget = ""
                                isDialogShown = false
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector =  Icons.Filled.Close,
                                contentDescription = null,
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                            Text(
                                text = stringResource(id = R.string.btn_cancel),
                                style = MaterialTheme.typography.body2
                            )
                        }
                        Button(
                            onClick = {
                                scope.launch {
                                    setBudget(preBudget.toFloat())
                                    preBudget = ""
                                    loadData()
                                }
                                isDialogShown = false
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector =  Icons.Filled.Done,
                                contentDescription = null,
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                            Text(
                                text = stringResource(id = R.string.btn_confirm),
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
            }
        }
    }
    Surface(
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 5.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { expanded = !expanded },
                    onLongPress = { isDialogShown = true }
                )
            }
    ) {
        if (!expanded) {
            Text(
                text = stringResource(id = R.string.expanding),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(5.dp),
                fontSize = 18.sp
            )
        }
        AnimatedVisibility(expanded) {
            Column(
                modifier = Modifier
                    .padding(5.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.monthly_expenses),
                    style = MaterialTheme.typography.body2
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RMBMount(
                        money = expenses,
                        hint = stringResource(id = R.string.no_expenses),
                        isHidden = eyeClosed
                    )
                    IconButton(
                        onClick = { eyeClosed = !eyeClosed },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Icon(
                            painter = if(eyeClosed) painterResource(id = R.drawable.ic_eye_closed)
                            else painterResource(id = R.drawable.eye_open),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.monthly_revenue),
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = stringResource(id = R.string.remained_budget),
                        style = MaterialTheme.typography.body2,
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RMBMount(
                        money = revenue,
                        hint = stringResource(id = R.string.no_revenue),
                        isHidden = eyeClosed
                    )
                    RMBMount(
                        money = budget,
                        isHidden = eyeClosed
                    )
                }
                Button(
                    onClick = openAnalysis,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = MaterialTheme.colors.primary
                    ),
                    elevation = null
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_profit),
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = stringResource(id = R.string.visualized_analysis),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}

@Composable
fun RMBMount(money: Float, hint: String = "¥0.00", isHidden: Boolean) {
    if(isHidden) {
        Text(
            text = stringResource(id = R.string.x),
            style = MaterialTheme.typography.h5
        )
    }
    else if(money == 0f) {
        Text(
            text = hint,
            style = MaterialTheme.typography.h5
        )
    }
    else {
        val amount = "¥" + String.format("%.2f", money)
        Text(
            text = amount,
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
fun BalanceToday(expenses_today: Float, revenue_today: Float) {
    val sb = StringBuilder()
    val infoToday = with (sb) {
        append(stringResource(id = R.string.daily_expenses))
        append("  ¥")
        append(String.format("%.2f", expenses_today))
        append("    ")
        append(stringResource(id = R.string.daily_revenue))
        append("  ¥")
        append(String.format("%.2f", revenue_today))
        toString()
    }
    Text(
        text = infoToday,
        style = MaterialTheme.typography.body1,
        fontSize = 18.sp,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Preview(
    name = "BalanceSurface",
    showBackground = true
)
@Composable
fun Preview() {
    Column() {
        BalanceSurface(expenses = 100.00f, revenue = 50.00f, budget = 200.00f, openAnalysis = {}, loadData = {})
        BalanceToday(expenses_today = 56.50f, revenue_today = 40.00f)
    }
}