package com.example.celebrare_assignment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Represents a serializable snapshot of a TextElement for undo/redo.
data class TextElementSnapshot(
    val id: Int,
    val text: String,
    val xFraction: Float,
    val yFraction: Float,
    val fontSize: Float,
    val fontFamily: String,
    val bold: Boolean,
    val italic: Boolean
)

// Represents a mutable text element on the canvas.
class TextElement(
    val id: Int,
    text: String,
    xFraction: Float,
    yFraction: Float,
    fontSize: Float = 28f,
    fontFamily: String = "Sans",
    bold: Boolean = false,
    italic: Boolean = false
) {
    var text by mutableStateOf(text)
    var xFraction by mutableStateOf(xFraction.coerceIn(0f, 1f))
    var yFraction by mutableStateOf(yFraction.coerceIn(0f, 1f))
    var fontSize by mutableStateOf(fontSize)
    var fontFamily by mutableStateOf(fontFamily)
    var bold by mutableStateOf(bold)
    var italic by mutableStateOf(italic)

    fun snapshot(): TextElementSnapshot =
        TextElementSnapshot(id, text, xFraction, yFraction, fontSize, fontFamily, bold, italic)

    companion object {
        fun fromSnapshot(s: TextElementSnapshot): TextElement = TextElement(
            s.id, s.text, s.xFraction, s.yFraction, s.fontSize, s.fontFamily, s.bold, s.italic
        )
    }
}