package com.example.celebrare_assignment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun CanvasScreen(
    modifier: Modifier = Modifier,
    state: CanvasState,
    backgroundColor: Color
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val screenWidth = constraints.maxWidth.toFloat()
        val screenHeight = constraints.maxHeight.toFloat()

        for (element in state.elements) {
            key(element.id) {
                val isSelected = state.selectedId == element.id
                DraggableTextResponsive(
                    element = element,
                    isSelected = isSelected,
                    screenWidth = screenWidth,
                    screenHeight = screenHeight,
                    onSelect = { state.selectedId = element.id },
                    onDoubleTap = { state.startEditing(element.id) },
                    onDragStart = { state.onDragStart() },
                    onDrag = { dxFraction, dyFraction ->
                        state.moveByFraction(element.id, dxFraction, dyFraction)
                    },
                    onSizeChanged = { width, height ->
                        state.updateElementSize(element.id, width, height)
                    }
                )
            }
        }
    }
}

@Composable
fun DraggableTextResponsive(
    element: TextElement,
    isSelected: Boolean,
    screenWidth: Float,
    screenHeight: Float,
    onSelect: () -> Unit,
    onDoubleTap: () -> Unit,
    onDragStart: () -> Unit,
    onDrag: (dxFraction: Float, dyFraction: Float) -> Unit,
    onSizeChanged: (widthFraction: Float, heightFraction: Float) -> Unit
) {
    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    (element.xFraction * screenWidth).roundToInt(),
                    (element.yFraction * screenHeight).roundToInt()
                )
            }
            .pointerInput(element.id) {
                detectDragGestures(
                    onDragStart = {
                        onDragStart()
                        onSelect()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val dxFraction = dragAmount.x / screenWidth
                        val dyFraction = dragAmount.y / screenHeight
                        onDrag(dxFraction, dyFraction)
                    }
                )
            }
            .pointerInput(element.id) {
                detectTapGestures(onTap = { onSelect() }, onDoubleTap = { onDoubleTap() })
            }
            .onSizeChanged { size ->
                val widthFraction = size.width / screenWidth
                val heightFraction = size.height / screenHeight
                onSizeChanged(widthFraction, heightFraction)
            }
    ) {
        Box(modifier = Modifier.padding(4.dp)) {
            Text(
                text = element.text,
                fontSize = element.fontSize.sp,
                fontFamily = mapFontFamily(element.fontFamily),
                fontWeight = if (element.bold) FontWeight.Bold else FontWeight.Normal,
                fontStyle = if (element.italic) FontStyle.Italic else FontStyle.Normal,
                textDecoration = if (element.underline) TextDecoration.Underline else null
            )
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .border(
                            width = 1.dp,
                            color = Color(0xFF1E88E5),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
        }
    }
}