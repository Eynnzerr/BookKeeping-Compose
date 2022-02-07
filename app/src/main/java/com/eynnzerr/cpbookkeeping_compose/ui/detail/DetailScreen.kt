package com.eynnzerr.cpbookkeeping_compose.ui.detail

import android.graphics.Rect
import android.graphics.Shader
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.eynnzerr.cpbookkeeping_compose.R
import com.eynnzerr.cpbookkeeping_compose.data.Bill
import com.eynnzerr.cpbookkeeping_compose.data.BillStatistic
import com.eynnzerr.cpbookkeeping_compose.data.billTypes
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlin.random.Random

// 首先获取本月度总支出与总收入，对当前月的数据求和即可。
// 接着，依次获取本月度各个天数的总支出、总收入，随同天数一起传给LineChart
// 接着，获取本月度各类型收入/支出的总和，随同类型一起传给PieChart
private const val TAG = "DetailScreen"

@Composable
fun DetailScreen(
    uiState: DetailUiState
) {
    MPPieChart(billData = uiState.pieDataMonthly)
}

/**
 * Draw a lineChart for dataList passed in with default 4 lines.
 * @param marginLeft left margin of the horizontal line(Not the text!) to the left side of the screen.
 * @param marginBottom bottom margin to the bottom of the screen.
 * @param marginRight right margin to the right side of the screen.
 * @param dataList list of data that is to be shown in chart. Should not be empty list!!!
 **/
@Composable
fun LineChart(
    marginLeft: Float,
    marginBottom: Float,
    marginRight: Float,
    dataList: List<Float>
) {
    val gridNumber = dataList.size - 1
    val maxValue = dataList.maxOrNull()
    val valuePartition = maxValue?.div(3)

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawIntoCanvas { canvas ->
            val lineWidth = size.width - marginLeft - marginRight
            val gridWidth = lineWidth / gridNumber
            val gridHeight = gridWidth - 40f

            val linePaint = Paint() //compose paint
            linePaint.apply {
                strokeWidth = 2f
                style = PaintingStyle.Stroke
                color = Color(188, 188, 188, 100)
            }
            val textPaint = android.graphics.Paint() //native paint
            textPaint.apply {
                strokeWidth = 2f
                style = android.graphics.Paint.Style.STROKE
                color = android.graphics.Color.argb(100, 111, 111, 111)
                textSize = 19f
            }

            // Rotate X-Y
            canvas.translate(0f, size.height)
            canvas.scale(1f, -1f)

            // Draw horizontal lines on background
            canvas.translate(marginLeft, marginBottom)
            val linePath = Path()
            linePath.lineTo(lineWidth, 0f)
            canvas.drawPath(linePath, linePaint)
            canvas.save()
            (0 until 3).forEach { _ ->
                canvas.translate(0f, gridHeight)
                canvas.drawPath(linePath, linePaint)
            }
            canvas.restore()

            // Draw text on X-Alias
            val rectText = Rect()
            canvas.save()
            canvas.scale(1f, -1f)
            (0 until gridNumber + 1).forEach{index ->
                if(index > 0) canvas.nativeCanvas.translate(gridWidth, 0f)
                val str = "01.${11 + index}" //TODO 换为真实数据源
                textPaint.getTextBounds(str, 0, str.length, rectText)
                canvas.nativeCanvas.drawText(
                    str,
                    -rectText.width().toFloat() / 2,
                    rectText.height().toFloat() * 2.5f,
                    textPaint
                )
            }
            canvas.restore()

            // Draw text on Y-Alias
            canvas.save()
            (0 until 4).forEach { index ->
                if (index > 0) canvas.translate(0f, gridHeight)
                val floatY = when(index) {
                    0 -> 0f
                    1 -> valuePartition
                    2 -> valuePartition!! * 2
                    else -> maxValue
                }
                val str = String.format("%.2f", floatY)

                canvas.save()
                canvas.scale(1f, -1f)
                textPaint.getTextBounds(str, 0, str.length, rectText)
                canvas.nativeCanvas.drawText(
                    str,
                    -rectText.width().toFloat() - 42f,
                    rectText.height().toFloat() / 2,
                    textPaint
                )
                canvas.restore()
            }
            canvas.restore()

            // Draw lines
            val text_paint = android.graphics.Paint()
            text_paint.apply {
                strokeWidth = 2f
                style = android.graphics.Paint.Style.FILL
                color = android.graphics.Color.argb(100, 111, 111, 111)
            }
            val caves_path = android.graphics.Path()
            val unitY = gridHeight / valuePartition!!
            val unitX = gridWidth
            val linearGradient = android.graphics.LinearGradient(
                0f,
                maxValue * unitY,
                0f,
                0f,
                android.graphics.Color.argb(255, 229, 160, 144),
                android.graphics.Color.argb(255, 251, 244, 240),
                Shader.TileMode.CLAMP
            )
            text_paint.shader = linearGradient
            for (index in 0 until dataList.size - 1) {
                val xMoveDistance = 20
                val yMoveDistance = 40

                if (dataList[index] == dataList[index + 1]) {
                    caves_path.lineTo(unitX * (index + 1), 0f)
                } else if (dataList[index] < dataList[index + 1]) {//y1<y2情况
                    val centerX = (gridWidth * index + gridWidth * (1 + index)) / 2
                    val centerY =
                        (dataList[index] * unitY + dataList[index + 1] * unitY) / 2
                    val controX0 = (gridWidth * index + centerX) / 2
                    val controY0 = (dataList[index] * unitY + centerY) / 2
                    val controX1 = (centerX + gridWidth * (1 + index)) / 2
                    val controY1 = (centerY + dataList[index + 1] * unitY) / 2
                    caves_path.cubicTo(
                        controX0 + xMoveDistance,
                        controY0 - yMoveDistance,
                        controX1 - xMoveDistance,
                        controY1 + yMoveDistance,
                        gridWidth * (1 + index),
                        dataList[index + 1] * unitY
                    )
                } else {
                    val centerX = (gridWidth * index + gridWidth * (1 + index)) / 2
                    val centerY =
                        (dataList[index] * unitY + dataList[index + 1] * unitY) / 2
                    val controX0 = (gridWidth * index + centerX) / 2
                    val controY0 = (dataList[index] * unitY + centerY) / 2
                    val controX1 = (centerX + gridWidth * (1 + index)) / 2
                    val controY1 = (centerY + dataList[index + 1] * unitY) / 2
                    caves_path.cubicTo(
                        controX0 + xMoveDistance,
                        controY0 + yMoveDistance,
                        controX1 - xMoveDistance,
                        controY1 - yMoveDistance,
                        gridWidth * (1 + index),
                        dataList[index + 1] * unitY
                    )

                }
            }
            canvas.nativeCanvas.drawCircle(0f, 0f, 10f, text_paint)
            //绘制闭合渐变曲线
            canvas.nativeCanvas.drawPath(caves_path, text_paint)
            val line_paint = android.graphics.Paint()
            line_paint.strokeWidth = 3f
            line_paint.style = android.graphics.Paint.Style.STROKE
            line_paint.color = android.graphics.Color.argb(255, 212, 100, 77)
            //绘制外环红色线
            canvas.nativeCanvas.drawPath(caves_path, line_paint)
            line_paint.style = android.graphics.Paint.Style.FILL
            //画圈。
            for (index in dataList.indices) {
                canvas.nativeCanvas.drawCircle(
                    gridWidth * index,
                    unitY * dataList[index],
                    8f,
                    line_paint
                )
            }
        }
    }
}

@Composable
fun PieChart(dataList: List<Float>) {
    //首先要根据各子项的值大小，计算其所占比例，作为其sweepAngle，同时还要将startAngle + sweepAngle作为新的startAngle
    var sum = 0f
    var startAngle = 0f
    for(data in dataList) {
        sum += data
    }
    val ratio = List(size = dataList.size) { index ->
        360f * dataList[index] / sum
    }
    Canvas(modifier = Modifier
        .size(300.dp)
    ) {
        val piePadding = size.width * 0.15f //change the radius of the circle
        (dataList.indices).forEach { index ->
            if(ratio[index] != 0f) {
                drawArc(
                    color = randomColor(),
                    startAngle = startAngle,
                    sweepAngle = ratio[index],
                    useCenter = true,
                    topLeft = Offset(piePadding * 2, piePadding * 6),
                    size = Size(size.width - piePadding * 2f, size.height - piePadding * 2f)
                )
                startAngle += ratio[index]
            }
        }
    }
}

private fun randomColor(): Color {
    val red = Random.nextFloat()
    val green = Random.nextFloat()
    val blue = Random.nextFloat()
    return Color(red, green, blue)
}

@Composable
private fun MPLineChart(billData: List<Bill>) {
    val entries = ArrayList<Entry>().apply {
        for (bill in billData) {
            //提取天数
        }
    }
}

@Composable
private fun MPPieChart(billData: List<BillStatistic>) {
    if (billData.isEmpty()) {
        Text(text = "暂无数据...")
    }
    else {
        val entries = ArrayList<PieEntry>().apply {
            for (statistic in billData){
                add(PieEntry(statistic.mValue, billTypes[statistic.mIndex]))
            }
        }
        val sliceColors = ArrayList<Int>().apply {
            for (color in ColorTemplate.VORDIPLOM_COLORS) add(color)
        }
        val dataSet = PieDataSet(entries, "").apply {
            colors = sliceColors
            sliceSpace = 2f
            selectionShift = 10f
            valueLineColor = android.graphics.Color.BLACK
            valueLinePart1OffsetPercentage = 80f
            valueLinePart1Length = 0.2f
            valueLinePart2Length = 0.4f
            xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        }
        val pieData = PieData(dataSet).apply {
            setDrawValues(true)
            setValueFormatter(PercentFormatter())
            setValueTextColor(android.graphics.Color.BLACK)
            setValueTextSize(40f)
        }
        AndroidView(
            factory = { context ->
                com.github.mikephil.charting.charts.PieChart(context).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)

                    setUsePercentValues(true)
                    description.isEnabled = false
                    legend.isEnabled = false
                    setBackgroundColor(android.graphics.Color.WHITE)
                    setExtraOffsets(40f,0f,40f,0f)
                    dragDecelerationFrictionCoef = 0.95f
                    rotationAngle = -30f
                    isRotationEnabled = true
                    isHighlightPerTapEnabled = true
                    animateY(500, Easing.EaseInQuad)

                    setDrawEntryLabels(true)
                    setEntryLabelColor(android.graphics.Color.BLACK)
                    setEntryLabelTextSize(15f)

                    isDrawHoleEnabled = true
                    holeRadius = 38f
                    transparentCircleRadius = 41f
                    setTransparentCircleColor(android.graphics.Color.BLACK)
                    setTransparentCircleAlpha(50)
                    setHoleColor(android.graphics.Color.WHITE)

                    data = pieData
                    highlightValues(null)
                    invalidate()
                }
            }
        )
    }
}

@Preview(
    name = "LineChart",
    showBackground = true
)
@Composable
fun PreviewLineChart() {
    LineChart(
        marginLeft = 180f,
        marginBottom = 240f,
        marginRight = 80f,
        dataList = listOf(0f,0f,0f,1000f,300f,600f,0f)
    )
}

@Preview(
    name = "PieChart",
    showBackground = true
)
@Composable
fun PreviewPieChart() {
    val dataList = listOf(23f, 15f, 30f, 32f)
    PieChart(dataList)
}

@Preview(
    name = "AndroidView",
    showBackground = true
)
@Composable
fun PreviewAndroidView() {
    val bills = listOf(
        BillStatistic(
            mIndex = R.drawable.type_other,
            mValue = 46f
        ),
        BillStatistic(
            mIndex = R.drawable.type_entertainment,
            mValue = 30f
        ),
        BillStatistic(
            mIndex = R.drawable.type_clothes,
            mValue = 60f
        ),
        BillStatistic(
            mIndex = R.drawable.type_daily,
            mValue = 64f
        )
    )
    MPPieChart(billData = bills)
}