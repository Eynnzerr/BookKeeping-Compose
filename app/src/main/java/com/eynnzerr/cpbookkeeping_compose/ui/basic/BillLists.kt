package com.eynnzerr.cpbookkeeping_compose.ui.basic

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.data.billTypes
import com.eynnzerr.cpbookkeeping_compose.R
import com.eynnzerr.cpbookkeeping_compose.data.fakeList

@Composable
fun BillList(bills: List<Bill>, listState: LazyListState) {
    //TODO 将传入的list作为数据源，绘制一个lazy column
    LazyColumn(
        state = listState
    ) {
        items(bills) { bill ->
            BillCard(bill = bill)
        } //items函数接收第一个参数list作为数据源，第二个参数lambda对每个item进行设置
    }
}

@Composable
fun BillCard(bill: Bill) {
    //TODO 组成列表的item，包括一个icon，4个text，点击可以跳转详情页，所以还需要传入一个onClick的lambda
    val tintColor = if(bill.category == -1) Color.Red else Color.Blue
    Surface(
        color = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 5.dp)
            .clickable {
                Log.d("BillLists", "BillCard: U clicked me!")
            }
    ) {
        Row(
            modifier = Modifier.padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /*Box(
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(80.dp)) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    drawCircle(
                        color = tintColor,
                        center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
                        radius = size.minDimension / 4
                    )
                }
                Icon(
                    painter = painterResource(id = bill.type),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White),
                    //TODO 加上选中变色(支出变红，收入变蓝)，常态灰白色的效果。且裁剪为圆形，有背景色和图标色
                    tint = tintColor
                )
            }*/
            Icon(
                painter = painterResource(id = bill.type),
                contentDescription = null,
                //TODO 加上选中变色(支出变红，收入变蓝)，常态灰白色的效果。且裁剪为圆形，有背景色和图标色
                tint = tintColor
            )
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))//spacer + padding 等价于原先的margin
            Text(
                text = billTypes[bill.type]!!,
                style = MaterialTheme.typography.body2
            )
            Spacer(modifier = Modifier.padding(horizontal = 110.dp))
            Column() {
                Text(
                    text = "¥" + String.format("%.2f", bill.amount),
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                Text(
                    text = bill.date,
                    textAlign = TextAlign.End
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


@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    name = "BillCard",
    showBackground = true
)
@Composable
fun PreviewBillList() {
    //BillList(bills = fakeList)
}
