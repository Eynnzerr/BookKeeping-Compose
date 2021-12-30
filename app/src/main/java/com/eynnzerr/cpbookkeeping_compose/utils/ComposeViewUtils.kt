package com.eynnzerr.cpbookkeeping_compose.utils

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview


private fun DrawScope.drawXLine(
    x_scaleWidth: Float,
    marginToLeft: Float,
    grid_width: Float,
    canvas: androidx.compose.ui.graphics.Canvas
) {
    var x_scaleWidth1 = x_scaleWidth
    var grid_width1 = grid_width
    val line_paint = Paint()
    line_paint.strokeWidth = 2f
    line_paint.style = PaintingStyle.Stroke
    line_paint.color = Color(188, 188, 188, 100)
    //x轴距离右边也留80距离
    x_scaleWidth1 = (size.width - marginToLeft - 80f)
    grid_width1 = x_scaleWidth1 / 6

    val onePath = Path()
    onePath.lineTo(x_scaleWidth1, 0f)
    canvas.drawPath(onePath, line_paint)

    canvas.save()
    //通过平移画布绘制剩余的平行x轴线
    (0 until 3).forEach { index ->
        canvas.translate(0f, grid_width1 - 40f)
        canvas.drawPath(onePath, line_paint)
    }
    canvas.restore()
}