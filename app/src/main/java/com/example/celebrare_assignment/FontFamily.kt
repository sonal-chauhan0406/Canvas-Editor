package com.example.celebrare_assignment

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

val availableFonts = listOf("Sans", "Roboto", "Lobster", "Poppins", "Oswald")

fun mapFontFamily(name: String): FontFamily = when (name) {
    "Sans" -> FontFamily.SansSerif
    "Serif" -> FontFamily.Serif
    "Roboto" -> FontFamily(Font(R.font.roboto_regular))
    "Lobster" -> FontFamily(Font(R.font.lobster))
    "Poppins" -> FontFamily(Font(R.font.poppins_regular))
    "Oswald" -> FontFamily(Font(R.font.oswald_regular))
    else -> FontFamily.Default
}