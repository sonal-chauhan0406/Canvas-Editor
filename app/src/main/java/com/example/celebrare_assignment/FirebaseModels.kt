package com.example.celebrare_assignment
import com.google.firebase.firestore.DocumentId


data class TextElementDTO(
    var id: Int = 0,
    var text: String = "",
    var xFraction: Float = 0f,
    var yFraction: Float = 0f,
    var fontSize: Float = 24f,
    var fontFamily: String = "Sans",
    var bold: Boolean = false,
    var italic: Boolean = false,
    var underline: Boolean = false,
    var widthFraction: Float = 0f,
    var heightFraction: Float = 0f
)

data class CanvasDataDTO(
    @DocumentId val documentId: String = "",
    val userId: String = "",
    val order: Int = 0,
    val color: Long = 0xFFFFFFFF,
    val elements: List<TextElementDTO> = emptyList()
)