package com.example.celebrare_assignment

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizePages(
    viewModel: CanvasViewModel,
    onDismiss: () -> Unit
) {
    val reorderablePages = remember { viewModel.canvasDataList.toMutableStateList() }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 8.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Manage Pages",
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(onClick = {
                        viewModel.addCanvasPage()
                        reorderablePages.clear()
                        reorderablePages.addAll(viewModel.canvasDataList)
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add New Page")
                    }
                }

                var draggedItem by remember { mutableStateOf<CanvasData?>(null) }
                var draggedItemInitialIndex by remember { mutableStateOf(-1) }
                var currentDragOffset by remember { mutableStateOf(0f) }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    itemsIndexed(reorderablePages, key = { _, page -> page.id }) { index, page ->
                        val isDragging = draggedItem?.id == page.id
                        val elevation = if (isDragging) 8.dp else 2.dp
                        val verticalOffset = if (isDragging) currentDragOffset else 0f

                        PageReorderItem(
                            pageIndex = index + 1,
                            pageData = page,
                            isDeleteEnabled = reorderablePages.size > 1,
                            onDeleteClick = {
                                viewModel.deleteCanvasPage(page)
                                reorderablePages.clear()
                                reorderablePages.addAll(viewModel.canvasDataList)
                            },
                            modifier = Modifier
                                .graphicsLayer { translationY = verticalOffset }
                                .pointerInput(page) {
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = {
                                            draggedItem = page
                                            draggedItemInitialIndex = index
                                        },
                                        onDragEnd = {
                                            draggedItem = null
                                            currentDragOffset = 0f
                                        },
                                        onDragCancel = {
                                            draggedItem = null
                                            currentDragOffset = 0f
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            currentDragOffset += dragAmount.y
                                            val initial = draggedItemInitialIndex
                                            val current = (initial + (currentDragOffset / (40.dp.toPx() + 8.dp.toPx()))).roundToInt()
                                                .coerceIn(0, reorderablePages.lastIndex)
                                            if (current != initial) {
                                                reorderablePages.add(current, reorderablePages.removeAt(initial))
                                                draggedItemInitialIndex = current
                                            }
                                        }
                                    )
                                },
                            elevation = elevation
                        )
                        Divider(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        viewModel.reorderCanvasPages(reorderablePages)
                        onDismiss()
                    }) {
                        Text("Done")
                    }
                }
            }
        }
    }
}

@Composable
fun PageReorderItem(
    pageIndex: Int,
    pageData: CanvasData,
    isDeleteEnabled: Boolean,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: Dp
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(pageData.color)
            )
            Spacer(Modifier.width(16.dp))
            Text(text = "Page $pageIndex", modifier = Modifier.weight(1f))
            IconButton(onClick = onDeleteClick, enabled = isDeleteEnabled) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Page")
            }
        }
    }
}