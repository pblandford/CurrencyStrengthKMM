package com.philblandford.currencystrength.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
fun CSTheme(content: @Composable () -> Unit) {
    val colorScheme = darkColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        Surface {
            content()
        }
    }
}