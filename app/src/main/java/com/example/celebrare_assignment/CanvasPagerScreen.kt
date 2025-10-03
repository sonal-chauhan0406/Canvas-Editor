package com.example.celebrare_assignment
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanvasPagerScreen(
    onSignOutClick: () -> Unit,
    viewModel: CanvasViewModel = viewModel()
) {
    val context = LocalContext.current

    val canvasDataList = viewModel.canvasDataList
    val isLoading = viewModel.isLoading

    var currentPageIndex by remember { mutableStateOf(0) }
    var showCustomizeDialog by remember { mutableStateOf(false) }
    var isControlsVisible by remember { mutableStateOf(true) }

    val totalPages = canvasDataList.size

    if (currentPageIndex >= totalPages && totalPages > 0) {
        currentPageIndex = totalPages - 1
    }

    val currentCanvasData = canvasDataList.getOrNull(currentPageIndex)

    if (showCustomizeDialog) {
        CustomizePages(
            viewModel = viewModel,
            onDismiss = { showCustomizeDialog = false }
        )
    }
    if (currentCanvasData != null) {
        when (val dialog = currentCanvasData.state.dialogState) {
            is DialogState.AddingText -> {
                EditTextDialog(
                    title = "Add New Text",
                    initialValue = "",
                    onConfirm = { newText ->
                        currentCanvasData.state.addTextElement(newText)
                        currentCanvasData.state.dismissDialog()
                    },
                    onDismiss = { currentCanvasData.state.dismissDialog() }
                )
            }
            is DialogState.EditingText -> {
                EditTextDialog(
                    title = "Edit Text",
                    initialValue = dialog.element.text,
                    onConfirm = { newText ->
                        currentCanvasData.state.updateTextElement(dialog.element.id, newText)
                        currentCanvasData.state.dismissDialog()
                    },
                    onDismiss = { currentCanvasData.state.dismissDialog() }
                )
            }
            is DialogState.Hidden -> { }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {  },
                actions = {
                    IconButton(onClick = {
                        viewModel.saveUserCanvases {
                            Toast.makeText(context, "Work Saved", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Default.Save, contentDescription = "Save Work")
                    }
                    IconButton(onClick = onSignOutClick) {
                        Icon(Icons.Default.Logout, contentDescription = "Sign Out")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            if (isControlsVisible && currentCanvasData != null) {
                BottomControls(
                    state = currentCanvasData.state,
                    onCustomizeClick = { showCustomizeDialog = true }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                if (currentCanvasData != null) {
                    CanvasScreen(
                        modifier = Modifier.matchParentSize(),
                        state = currentCanvasData.state,
                        backgroundColor = currentCanvasData.color
                    )
                }

                val canGoLeft = currentPageIndex > 0
                val canGoRight = currentPageIndex < totalPages - 1

                IconButton(
                    onClick = { if (canGoLeft) currentPageIndex-- },
                    enabled = canGoLeft,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .zIndex(1f)
                        .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Previous Canvas",
                        tint = if (canGoLeft) MaterialTheme.colorScheme.onSurface else Color.Gray,
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(
                    onClick = { if (canGoRight) currentPageIndex++ },
                    enabled = canGoRight,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .zIndex(1f)
                        .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next Canvas",
                        tint = if (canGoRight) MaterialTheme.colorScheme.onSurface else Color.Gray,
                        modifier = Modifier.size(36.dp)
                    )
                }

                if (totalPages > 0) {
                    Text(
                        text = "Canvas ${currentPageIndex + 1} of $totalPages",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 8.dp)
                            .background(Color.LightGray.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                IconButton(
                    onClick = { isControlsVisible = !isControlsVisible },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                        .zIndex(1f)
                ) {
                    Icon(
                        imageVector = if (isControlsVisible) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = if (isControlsVisible) "Hide Controls" else "Show Controls"
                    )
                }
            }
        }
    }
}