package com.philblandford.currencystrength.features.chart.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.philblandford.currencystrength.common.log.log


data class DataSet(val name: String, val data: List<Float>, val color: Color)

@Composable
fun Chart(modifier: Modifier, dataSets: List<DataSet>) {

    Canvas(modifier) {
         dataSets.flatMap { it.data }.maxOfOrNull { kotlin.math.abs(it) }?.let { maxAbsolute ->
             dataSets.forEach {
                 drawLine(it, maxAbsolute)
             }
         }
    }
}

fun DrawScope.drawLine(dataSet: DataSet, maxAbsolute:Float) {
    val width = size.width
    val height = size.height

    val minValue = -maxAbsolute
    val step = width / (dataSet.data.size - 1)

    val path = Path().apply {
        dataSet.data.forEachIndexed { index, value ->
            val x = index * step
            val y = height - ((value - minValue) / (maxAbsolute - minValue)) * height
            if (index == 0) moveTo(x, y) else lineTo(x, y)
        }
    }

    drawPath(path, color = dataSet.color, style = Stroke(width = 4f))

}
