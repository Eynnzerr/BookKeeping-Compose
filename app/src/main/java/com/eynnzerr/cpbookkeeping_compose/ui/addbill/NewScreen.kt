package com.eynnzerr.cpbookkeeping_compose.ui.addbill

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eynnzerr.cpbookkeeping_compose.R
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.data.billTypes
import com.eynnzerr.cpbookkeeping_compose.data.expenseList
import com.eynnzerr.cpbookkeeping_compose.data.revenueList
import com.eynnzerr.cpbookkeeping_compose.ui.theme.Blue_2
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate


enum class Sections(@StringRes val titleResId: Int) {
    NewRevenue(R.string.section_new_revenue),
    NewExpense(R.string.section_new_expense)
}

/**
 * TabContent for a single tab of the screen.
 *
 * This is intended to encapsulate a tab & it's content as a single object. It was added to avoid
 * passing several parameters per-tab from the stateful composable to the composable that displays
 * the current tab.
 *
 * @param section the tab that this content is for
 * @param content content of the tab, a composable that describes the content
 */
class TabContent(val section: Sections, val content: @Composable () -> Unit)

/**
 * Stateless screen displays the tabs specified in [tabContent]
 *
 * @param tabContent (slot) the tabs and their content to display on this screen, must be a
 * non-empty list, tabs are displayed in the order specified by this list
 * @param currentSection (state) the current tab to display, must be in [tabContent]
 * @param onTabChange (event) request a change in [currentSection] to another tab from [tabContent]
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewScreen(
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    remark: String,
    tabContent: List<TabContent>,
    currentSection: Sections,
    onTabChange: (Sections) -> Unit,
    onRemarkChange: (String) -> Unit
) {
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            TextField(
                value = remark,
                onValueChange = { onRemarkChange(it) },
                label = { Text(text = stringResource(id = R.string.add_remark)) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            Row(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    shape = RoundedCornerShape(size = 5.dp),
                    onClick = {
                    scope.launch {
                        onRemarkChange("添加备注")
                        scaffoldState.bottomSheetState.collapse()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(id = R.string.btn_cancel))
                }
                Button(
                    shape = RoundedCornerShape(size = 5.dp),
                    onClick = {
                    scope.launch {
                        scaffoldState.bottomSheetState.collapse()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(id = R.string.btn_confirm))
                }
            }
        }
    ) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)
        NewScreenContent(
            currentSection = currentSection,
            updateSection = onTabChange,
            tabContent = tabContent,
            modifier = screenModifier
        )
    }
}

/**
 * Remembers the content for each tab on the Interests screen
 */
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterialApi
@Composable
fun rememberTabContent(
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    expenseState: TabState,
    revenueState: TabState,
    onUpdate: (Int, String, Int) -> Unit,
    remark: String,
    onRemarkChange: (String) -> Unit,
    onSubmit: (Bill) -> Unit
): List<TabContent> {
    // Describe the screen sections here
    // Pass them to the stateless NewScreen using a tabContent.
    val expenseSection = TabContent(Sections.NewExpense) {
        TabWithSections(
            category = -1,
            scaffoldState = scaffoldState,
            scope = scope,
            tabState = expenseState,
            onUpdate = onUpdate,
            remark = remark,
            onRemarkChange = onRemarkChange,
            onSubmit = onSubmit
        )
    }

    val revenueSection = TabContent(Sections.NewRevenue) {
        TabWithSections(
            category = 1,
            scaffoldState = scaffoldState,
            scope = scope,
            tabState = revenueState,
            onUpdate = onUpdate,
            remark = remark,
            onRemarkChange = onRemarkChange,
            onSubmit = onSubmit
        )
    }

    return listOf(expenseSection, revenueSection)
}

/**
 * Display a row of tab items and their corresponding content below.
 * @param currentSection (state) the tab that is currently selected.
 * @param updateSection (event) request for a change in tab selection.
 * @param tabContent (slot) a list of tabs and their content.Must be non-bull.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun NewScreenContent(
    currentSection: Sections,
    updateSection: (Sections) -> Unit,
    tabContent: List<TabContent>,
    modifier: Modifier = Modifier
) {
    val selectedTabIndex = tabContent.indexOfFirst { it.section == currentSection }
    Column(modifier) {
        NewTabRow(selectedTabIndex, updateSection, tabContent)

        Box(modifier = Modifier.weight(1f)) {
            // display the current tab content which is a @Composable () -> Unit
            tabContent[selectedTabIndex].content()
        }
    }
}

/**
 * TabRow for the InterestsScreen
 */
@Composable
private fun NewTabRow(
    selectedTabIndex: Int,
    updateSection: (Sections) -> Unit,
    tabContent: List<TabContent>
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = MaterialTheme.colors.onPrimary,
        contentColor = MaterialTheme.colors.primary
    ) {
        NewTabRowContent(selectedTabIndex, updateSection, tabContent)
    }
}

@Composable
private fun NewTabRowContent(
    selectedTabIndex: Int,
    updateSection: (Sections) -> Unit,
    tabContent: List<TabContent>,
    modifier: Modifier = Modifier
) {
    tabContent.forEachIndexed { index, content ->
        val colorText = if (selectedTabIndex == index) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
        }
        Tab(
            selected = selectedTabIndex == index,
            onClick = { updateSection(content.section) },
            modifier = Modifier.heightIn(min = 48.dp)
        ) {
            Text(
                text = stringResource(id = content.section.titleResId),
                color = colorText,
                style = MaterialTheme.typography.subtitle2,
                modifier = modifier.paddingFromBaseline(top = 20.dp)
            )
        }
    }
}

/**
 * Display content for creating new bill, composed of TypeSelections and Calculator with SpaceBetween Column.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TabWithSections(
    category: Int,
    remark: String,
    tabState: TabState,
    onUpdate: (Int, String, Int) -> Unit,
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    onRemarkChange: (String) -> Unit,
    onSubmit: (Bill) -> Unit
) {
    val amountBuilder by remember { mutableStateOf(StringBuilder(tabState.amount)) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TypeSelections(
            category = category,
            bills = if(category == -1) expenseList else revenueList,
            amount = tabState.amount,
            selectedIndex = tabState.selectedIndex,
            onUpdate = {index -> onUpdate(category, tabState.amount, index)} //only modify index
        )
        Calculator(
            bills = if(category == -1) expenseList else revenueList,
            category = category,
            tabState = tabState,
            onAmountChange = {amount -> onUpdate(category, amount, tabState.selectedIndex)},// only modify amount
            amountBuilder = amountBuilder,
            remark = remark,
            scaffoldState = scaffoldState,
            scope = scope,
            onRemarkChange = onRemarkChange,
            onSubmit = onSubmit
        )
    }
}

/**
 * composable for selecting specific type of bill
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TypeSelections(
    category: Int,
    bills: List<Bill>,
    amount: String,
    selectedIndex: Int,
    onUpdate: (Int) -> Unit
) {
    val tintColor = if(category == -1) Color.Red else Color.Blue
    Surface(
        elevation = 10.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(all = 5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(
                        painter = painterResource(id = bills[selectedIndex].type),
                        contentDescription = null,
                        tint = tintColor,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(
                        text = billTypes[bills[selectedIndex].type]!!,
                        style = MaterialTheme.typography.body2
                    )
                }
                Text(
                    text = amount,
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.End
                )
            }
            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
            )
            //TODO 实现一个横向可滑动的双行列表???
            LazyRow(){
                itemsIndexed(bills) { index, bill ->
                    TypeListItem(
                        resId = bill.type,
                        typeName = billTypes[bill.type]!!,
                        color = tintColor
                    ) {
                        onUpdate(index)
                        Log.d("TypeSelections", "selectedIndex: $index")
                    }
                }
            }
        }
    }
}

@Composable
private fun TypeListItem(resId: Int, typeName: String, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        // a icon and a text
        Icon(
            painter = painterResource(id = resId),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(48.dp)
        )
        Text(text = typeName, modifier = Modifier.padding(top = 5.dp))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun Calculator(
    bills: List<Bill>,
    category: Int,
    tabState: TabState,
    onAmountChange: (String) -> Unit,
    amountBuilder: StringBuilder,
    remark: String,
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    onRemarkChange: (String) -> Unit,
    onSubmit: (Bill) ->Unit
) {
    var date by remember { mutableStateOf(LocalDate.now().toString()) } //date 需要
    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(stringResource(id = R.string.btn_confirm))
            negativeButton(stringResource(id = R.string.btn_cancel))
        }
    ) {
        datepicker { selectedDate ->
            date = selectedDate.toString()
        }
    }
    Surface(
        elevation = 10.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            //verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = {
                    if(remark == "添加备注") onRemarkChange("")
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                }) {
                    Text(text = remark)
                }
                TextButton(onClick = {
                    dialogState.show()
                }) {
                    Text(text = date)
                }
            }
            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    CalculatorButton(
                        text = "1"
                    ) {
                        if(amountBuilder.lastIndex == 1 && amountBuilder[1] == '0') amountBuilder.deleteAt(1) //remove extra zero
                        amountBuilder.append(1)
                        onAmountChange(amountBuilder.toString())
                        Log.d("NewScreen", "Calculator: amount:$amountBuilder")
                    }
                    CalculatorButton(
                        text = "4"
                    ) {
                        if(amountBuilder.lastIndex == 1 && amountBuilder[1] == '0') amountBuilder.deleteAt(1) //remove extra zero
                        amountBuilder.append(4)
                        onAmountChange(amountBuilder.toString())
                        Log.d("NewScreen", "Calculator: amount:$amountBuilder")
                    }
                    CalculatorButton(
                        text = "7"
                    ) {
                        if(amountBuilder.lastIndex == 1 && amountBuilder[1] == '0') amountBuilder.deleteAt(1) //remove extra zero
                        amountBuilder.append(7)
                        onAmountChange(amountBuilder.toString())
                        Log.d("NewScreen", "Calculator: amount:$amountBuilder")
                    }
                    CalculatorButton(
                        text = stringResource(id = R.string.btn_reset)
                    ) {
                        amountBuilder.delete(0, amountBuilder.length)
                        amountBuilder.append("¥0")
                        onAmountChange(amountBuilder.toString())
                        Log.d("NewScreen", "Calculator: amount:$amountBuilder")
                    }
                }
                Column {
                    CalculatorButton(
                        text = "2"
                    ) {
                        if(amountBuilder.lastIndex == 1 && amountBuilder[1] == '0') amountBuilder.deleteAt(1) //remove extra zero
                        amountBuilder.append(2)
                        onAmountChange(amountBuilder.toString())
                        Log.d("NewScreen", "Calculator: amount:$amountBuilder")
                    }
                    CalculatorButton(
                        text = "5"
                    ) {
                        if(amountBuilder.lastIndex == 1 && amountBuilder[1] == '0') amountBuilder.deleteAt(1) //remove extra zero
                        amountBuilder.append(5)
                        onAmountChange(amountBuilder.toString())
                        Log.d("NewScreen", "Calculator: amount:$amountBuilder")
                    }
                    CalculatorButton(
                        text = "8"
                    ) {
                        if(amountBuilder.lastIndex == 1 && amountBuilder[1] == '0') amountBuilder.deleteAt(1) //remove extra zero
                        amountBuilder.append(8)
                        onAmountChange(amountBuilder.toString())
                        Log.d("NewScreen", "Calculator: amount:$amountBuilder")
                    }
                    CalculatorButton(
                        text = "0"
                    ) {
                        if(!(amountBuilder.lastIndex == 1 && amountBuilder[1] == '0')) amountBuilder.append(0)
                        onAmountChange(amountBuilder.toString())
                        Log.d("NewScreen", "Calculator: amount:$amountBuilder")
                    }
                }
                Column {
                    CalculatorButton(
                        text = "3"
                    ) {
                        if(amountBuilder.lastIndex == 1 && amountBuilder[1] == '0') amountBuilder.deleteAt(1) //remove extra zero
                        amountBuilder.append(3)
                        onAmountChange(amountBuilder.toString())
                        Log.d("NewScreen", "Calculator: amount:$amountBuilder")
                    }
                    CalculatorButton(
                        text = "6"
                    ) {
                        if(amountBuilder.lastIndex == 1 && amountBuilder[1] == '0') amountBuilder.deleteAt(1) //remove extra zero
                        amountBuilder.append(6)
                        onAmountChange(amountBuilder.toString())
                        Log.d("NewScreen", "Calculator: amount:$amountBuilder")
                    }
                    CalculatorButton(
                        text = "9"
                    ) {
                        if(amountBuilder.lastIndex == 1 && amountBuilder[1] == '0') amountBuilder.deleteAt(1) //remove extra zero
                        amountBuilder.append(9)
                        onAmountChange(amountBuilder.toString())
                        Log.d("NewScreen", "Calculator: amount:$amountBuilder")
                    }
                    CalculatorButton(
                        text = "."
                    ) {
                        if(!amountBuilder[amountBuilder.lastIndex].equals(".")) amountBuilder.append(".")
                        onAmountChange(amountBuilder.toString())
                        Log.d("NewScreen", "Calculator: amount:$amountBuilder")
                    }
                }
                Column {
                    CalculatorIconButton(
                        imageVector = Icons.Filled.Cancel
                    ) {
                        if (amountBuilder.lastIndex > 1) {
                            amountBuilder.deleteAt(amountBuilder.lastIndex)
                        } else {
                            amountBuilder.replace(1, 2, "0")
                        }
                        onAmountChange(amountBuilder.toString())
                        Log.d("NewScreen", "Calculator: amount:$amountBuilder")
                    }
                    CalculatorButton(
                        text = stringResource(id = R.string.btn_confirm)
                    ) {
                        val bill = Bill(
                            type = bills[tabState.selectedIndex].type,
                            amount = tabState.amount.substring(1).toFloat(),
                            remark = remark,
                            date = date,
                            category = category
                        )
                        onSubmit(bill)
                    }
                }
            }
        }
    }
}

@Composable
private fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionState = remember { MutableInteractionSource() }
    val buttonColor = when {
        interactionState.collectIsPressedAsState().value -> Blue_2
        else -> MaterialTheme.colors.primary
    }
    Button(
        interactionSource = interactionState,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = buttonColor,
        ),
        shape = RoundedCornerShape(size = 5.dp),
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = text)
    }
}

@Composable
private fun CalculatorIconButton(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionState = remember { MutableInteractionSource() }
    val buttonColor = when {
        interactionState.collectIsPressedAsState().value -> Blue_2
        else -> MaterialTheme.colors.primary
    }
    Button(
        interactionSource = interactionState,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = buttonColor,
        ),
        shape = RoundedCornerShape(size = 5.dp),
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null
        )
    }
}

/*@Preview(
    name = "TypeSelections",
    showBackground = true
)
@Composable
private fun PreviewTypeSelections() {
    //TypeSelections(category = 1, bills = fakeList, amount = "0.00")
}*/
