package com.eynnzerr.cpbookkeeping_compose.ui.new

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eynnzerr.cpbookkeeping_compose.R
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.data.billTypes
import com.eynnzerr.cpbookkeeping_compose.ui.home.BalanceSurface
import com.eynnzerr.cpbookkeeping_compose.ui.home.BalanceToday

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
@Composable
fun NewScreen(
    tabContent: List<TabContent>,
    currentSection: Sections,
    onTabChange: (Sections) -> Unit
) {
    Scaffold(
        topBar = {
/*            TopAppBar(
                title = {
                    Text(text = "test")
                },
                navigationIcon = {
                    IconButton(onClick = { *//*TODO back to the last screen*//* }) {
                        Icon(
                            imageVector = Icons.Filled.Error,
                            contentDescription = stringResource(id = R.string.cancel_new)
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp
            )*/
        }
    ) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)
        NewScreenContent(currentSection = currentSection, updateSection = onTabChange, tabContent = tabContent, modifier = screenModifier)
    }
}

/**
 * Remembers the content for each tab on the Interests screen
 */
@Composable
fun rememberTabContent(): List<TabContent> {
    // Describe the screen sections here
    // Pass them to the stateless NewScreen using a tabContent.
    val expenseSection = TabContent(Sections.NewExpense) {
        TabWithSections(
            category = -1
        )
    }

    val revenueSection = TabContent(Sections.NewRevenue) {
        TabWithSections(
            category = 1
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
 * Display content for creating new bill
 */
@Composable
private fun TabWithSections(category: Int) {
    val text = if(category == -1) "expense" else "revenue"
    Text(text = text)
}

/**
 * composable for selecting specific type of bill
 */
@Composable
private fun TypeSelections(category: Int, bills: List<Bill>, amount: String) {
    val tintColor = if(category == -1) Color.Red else Color.Blue
    val selectedIndex by remember { mutableStateOf(0) }
    Surface(
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Row(
                modifier = Modifier.padding(all = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = bills[selectedIndex].type),
                    contentDescription = null,
                    tint = tintColor
                )
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))//spacer + padding 等价于原先的margin
                Text(
                    text = billTypes[bills[selectedIndex].type]!!,
                    style = MaterialTheme.typography.body2
                )
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
        }
    }
}

@Composable
private fun TypeListItem(resId: Int, typeName: String) {
    val interactionState = remember { MutableInteractionSource() }
    val tintColor = when {
        interactionState.collectIsPressedAsState().value -> Color.Red
        else -> Color.Gray
    }
    Column {
        // a icon and a text
    }
}



@Preview(
    name = "NewScreen",
    showBackground = true
)
@Composable
fun Preview() {

}
