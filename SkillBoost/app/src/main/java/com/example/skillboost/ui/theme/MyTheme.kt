package com.example.skillboost.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.CompositionLocalProvider

private val LightColorPalette = lightColorScheme(
    primary = Color(0xFF009F77),
    onPrimary = Color.White,
    secondary =  Color(0xFF009F77)
)

private val DarkColorPalette = darkColorScheme(
    primary = Color(0xFF009F77),
    onPrimary = Color.White,
    secondary =  Color(0xFF009F77)
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyTheme(colorScheme: ColorScheme = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette, content: @Composable() () -> Unit) {
    MaterialTheme(
        colorScheme = colorScheme,
        content = {
            CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                content()
            }
        }
    )
}

