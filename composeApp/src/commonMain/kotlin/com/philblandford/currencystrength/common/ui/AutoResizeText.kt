package com.philblandford.currencystrength.common.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun AutoResizeText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    maxLines: Int = 1,
    minFontSize: TextUnit = 12.sp,
    stepGranularity: Float = 0.9f
) {
    var currentFontSize by remember { mutableStateOf(style.fontSize) }
    var readyToDraw by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier) {
        // If not ready, try drawing with the current font size to measure overflow.
        if (!readyToDraw) {
            Text(
                text = text,
                style = style.copy(fontSize = currentFontSize),
                maxLines = maxLines,
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.hasVisualOverflow && currentFontSize > minFontSize) {
                        // Reduce font size and trigger recomposition.
                        currentFontSize = (currentFontSize.value * stepGranularity).sp
                    } else {
                        readyToDraw = true
                    }
                }
            )
        } else {
            // Once the text fits, draw it normally.
            Text(
                text = text,
                style = style.copy(fontSize = currentFontSize),
                maxLines = maxLines
            )
        }
    }
}