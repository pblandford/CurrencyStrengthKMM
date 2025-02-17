package com.philblandford.currencystrength.features.chart.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke


data class DataSet(val name: String, val data: List<Float>, val color: Color)

@Composable
fun Chart(modifier: Modifier, dataSets: List<DataSet>) {
    Canvas(modifier) {
        dataSets.forEach {
            drawLine(it)
        }
    }
}

fun DrawScope.drawLine(dataSet: DataSet) {
    val width = size.width
    val height = size.height

    val maxAbsolute = dataSet.data.maxOf { kotlin.math.abs(it) }

    val minValue = -maxAbsolute
    val maxValue = maxAbsolute

    val path = Path().apply {

        dataSet.data.forEachIndexed { index, value ->
            val x = index * (width / dataSet.data.size)
            val y = height - ((value - minValue) / (maxValue - minValue)) * height
            if (index == 0) moveTo(x, y) else lineTo(x, y)
        }
    }

    drawPath(path, color = dataSet.color, style = Stroke(width = 4f))

}
