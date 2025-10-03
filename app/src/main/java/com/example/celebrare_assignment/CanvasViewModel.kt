package com.example.celebrare_assignment

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CanvasViewModel : ViewModel() {
    private val repository = CanvasRepository()
    val canvasDataList = mutableStateListOf<CanvasData>()
    var isLoading by mutableStateOf(true)
        private set
    private val defaultColors = listOf(
        Color(0xFFD8D1E1),
        Color(0xFFECE8CD),
        Color(0xFFEECBCB),
        Color(0xFFD1E7E7),
        Color(0xFFFDECC8)
    )

    init {
        loadUserCanvases()
    }

    fun loadUserCanvases() {
        viewModelScope.launch {
            isLoading = true
            val loadedCanvases = repository.loadCanvases()
            canvasDataList.clear()
            if (loadedCanvases.isNotEmpty()) {
                canvasDataList.addAll(loadedCanvases)
            } else {
                canvasDataList.add(CanvasData(id = 1, color = Color(0xFFD8D1E1)))
            }
            isLoading = false
        }
    }

    fun saveUserCanvases(onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.saveCanvases(canvasDataList.toList())
                Log.d("Firestore", "Canvases saved successfully!")
                onComplete()
            } catch (e: Exception) {
                Log.e("Firestore", "Error saving canvases", e)
            }
        }
    }

    fun addCanvasPage() {
        val nextId = (canvasDataList.maxOfOrNull { it.id } ?: 0) + 1
        val nextColor = defaultColors[canvasDataList.size % defaultColors.size]
        canvasDataList.add(CanvasData(id = nextId, color = nextColor))
    }

    fun deleteCanvasPage(pageToDelete: CanvasData) {
        if (canvasDataList.size > 1) {
            canvasDataList.remove(pageToDelete)
        }
    }

    fun reorderCanvasPages(reorderedPages: List<CanvasData>) {
        canvasDataList.clear()
        canvasDataList.addAll(reorderedPages)
    }
}