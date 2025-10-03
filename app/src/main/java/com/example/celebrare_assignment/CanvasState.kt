package com.example.celebrare_assignment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class TextElement(
    val id: Int = 0,
    var text: String = "",
    var xFraction: Float = 0.3f,
    var yFraction: Float = 0.4f,
    var fontSize: Float = 24f,
    var fontFamily: String = "Sans",
    var bold: Boolean = false,
    var italic: Boolean = false,
    var underline: Boolean = false,
    var widthFraction: Float = 0f,
    var heightFraction: Float = 0f
)

sealed class DialogState {
    object Hidden : DialogState()
    object AddingText : DialogState()
    data class EditingText(val element: TextElement) : DialogState()
}

class CanvasState {
    var elements = mutableStateListOf<TextElement>()
        private set

    private val undoStack = mutableListOf<List<TextElement>>()
    private val redoStack = mutableListOf<List<TextElement>>()

    var selectedId by mutableStateOf<Int?>(null)
    var dialogState by mutableStateOf<DialogState>(DialogState.Hidden)
        private set

    private var nextId = 0

    private fun saveStateForUndo() {
        redoStack.clear()
        undoStack.add(elements.toList())
    }

    fun showAddTextDialog() {
        dialogState = DialogState.AddingText
    }

    fun showEditTextDialog() {
        getSelectedElement()?.let {
            dialogState = DialogState.EditingText(it)
        }
    }

    fun startEditing(elementId: Int) {
        elements.find { it.id == elementId }?.let {
            dialogState = DialogState.EditingText(it)
        }
    }

    fun dismissDialog() {
        dialogState = DialogState.Hidden
    }

    fun addTextElement(text: String) {
        if (text.isNotBlank()) {
            saveStateForUndo()
            elements.add(TextElement(id = nextId++, text = text))
        }
    }

    fun updateTextElement(id: Int, newText: String) {
        elements.find { it.id == id }?.let { element ->
            saveStateForUndo()
            val index = elements.indexOf(element)
            if (index != -1) {
                elements[index] = element.copy(text = newText)
            }
        }
    }

    fun getSelectedElement(): TextElement? = elements.find { it.id == selectedId }

    fun removeSelected() {
        selectedId?.let { id ->
            saveStateForUndo()
            elements.removeIf { it.id == id }
            selectedId = null
        }
    }

    fun updateSelected(update: TextElement.() -> Unit) {
        getSelectedElement()?.let { element ->
            saveStateForUndo()
            val index = elements.indexOf(element)
            if (index != -1) {
                val updatedElement = element.copy().apply(update)
                elements[index] = updatedElement
            }
        }
    }

    fun onDragStart() {
        saveStateForUndo()
    }

    fun updateElementSize(id: Int, widthFraction: Float, heightFraction: Float) {
        elements.find { it.id == id }?.let { element ->
            if (element.widthFraction != widthFraction || element.heightFraction != heightFraction) {
                val index = elements.indexOf(element)
                if (index != -1) {
                    elements[index] = element.copy(
                        widthFraction = widthFraction,
                        heightFraction = heightFraction
                    )
                }
            }
        }
    }

    fun moveByFraction(id: Int, dxFraction: Float, dyFraction: Float) {
        elements.find { it.id == id }?.let { element ->
            val index = elements.indexOf(element)
            if (index != -1) {
                val newX = element.xFraction + dxFraction
                val newY = element.yFraction + dyFraction

                val maxX = (1.0f - element.widthFraction).coerceAtLeast(0f)
                val maxY = (1.0f - element.heightFraction).coerceAtLeast(0f)

                val constrainedX = newX.coerceIn(0f, maxX)
                val constrainedY = newY.coerceIn(0f, maxY)

                elements[index] = element.copy(
                    xFraction = constrainedX,
                    yFraction = constrainedY
                )
            }
        }
    }

    fun canUndo(): Boolean = undoStack.isNotEmpty()
    fun canRedo(): Boolean = redoStack.isNotEmpty()

    fun undo() {
        if (canUndo()) {
            val currentState = elements.toList()
            redoStack.add(currentState)
            val previousState = undoStack.removeAt(undoStack.lastIndex)
            elements.clear()
            elements.addAll(previousState)
        }
    }

    fun redo() {
        if (canRedo()) {
            val currentState = elements.toList()
            undoStack.add(currentState)
            val nextState = redoStack.removeAt(redoStack.lastIndex)
            elements.clear()
            elements.addAll(nextState)
        }
    }
}