package com.example.celebrare_assignment
import androidx.compose.ui.graphics.Color
data class CanvasData(
    val id: Int,
    val state: CanvasState = CanvasState(),
    val color: Color
)
