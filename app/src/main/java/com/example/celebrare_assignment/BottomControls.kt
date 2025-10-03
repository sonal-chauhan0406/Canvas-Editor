package com.example.celebrare_assignment
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun BottomControls(
    state: CanvasState,
    onCustomizeClick: () -> Unit
) {
    val selected = state.getSelectedElement()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(top = 8.dp, bottom = 55.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ActionIconButton(
                onClick = { state.showAddTextDialog() },
                icon = Icons.Default.TextFields,
                label = "Add Text"
            )
            ActionIconButton(
                onClick = { state.showEditTextDialog() },
                icon = Icons.Default.Edit,
                label = "Edit Text",
                enabled = selected != null
            )
            ActionIconButton(
                onClick = { state.removeSelected() },
                icon = Icons.Default.Delete,
                label = "Delete",
                enabled = selected != null
            )
            ActionIconButton(
                onClick = onCustomizeClick,
                icon = Icons.Default.Layers,
                label = "Pages"
            )
            ActionIconButton(
                onClick = { state.undo() },
                icon = Icons.Default.Undo,
                label = "Undo",
                enabled = state.canUndo()
            )
            ActionIconButton(
                onClick = { state.redo() },
                icon = Icons.Default.Redo,
                label = "Redo",
                enabled = state.canRedo()
            )
        }
        if (selected != null) {
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Size:", fontWeight = FontWeight.Bold)
                    Slider(
                        value = selected.fontSize,
                        onValueChange = { newSize -> state.updateSelected { fontSize = newSize } },
                        valueRange = 12f..96f,
                        modifier = Modifier.weight(1f)
                    )
                    Text("${selected.fontSize.roundToInt()}sp")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Text("Style:", fontWeight = FontWeight.Bold)

                    IconToggleButton(
                        checked = selected.bold,
                        onCheckedChange = { state.updateSelected { bold = !bold } }
                    ) {
                        Icon(Icons.Default.FormatBold, contentDescription = "Bold")
                    }

                    IconToggleButton(
                        checked = selected.italic,
                        onCheckedChange = { state.updateSelected { italic = !italic } }
                    ) {
                        Icon(Icons.Default.FormatItalic, contentDescription = "Italic")
                    }
                    IconToggleButton(
                        checked = selected.underline,
                        onCheckedChange = { state.updateSelected { underline = !underline } }) {
                        Icon(Icons.Default.FormatUnderlined, contentDescription = "Underline")
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    var fontMenuExpanded by remember { mutableStateOf(false) }
                    Box {
                        TextButton(onClick = { fontMenuExpanded = true }) {
                            Icon(Icons.Default.FontDownload, contentDescription = "Select Font Family", modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(selected.fontFamily)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Open font menu")
                        }
                        DropdownMenu(
                            expanded = fontMenuExpanded,
                            onDismissRequest = { fontMenuExpanded = false }
                        ) {
                            availableFonts.forEach { fontName ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = fontName, fontFamily = mapFontFamily(fontName))
                                    },
                                    onClick = {
                                        state.updateSelected { fontFamily = fontName }
                                        fontMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    enabled: Boolean = true
) {
    val contentColor = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        IconButton(onClick = onClick, enabled = enabled) {
            Icon(icon, contentDescription = label, tint = contentColor)
        }
        Text(
            text = label,
            fontSize = 12.sp,
            color = contentColor
        )
    }
}