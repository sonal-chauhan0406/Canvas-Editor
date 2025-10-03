package com.example.celebrare_assignment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class CanvasRepository {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private fun getCurrentUserId(): String? = auth.currentUser?.uid

    suspend fun saveCanvases(canvases: List<CanvasData>) {
        val userId = getCurrentUserId() ?: return

        val collectionRef = db.collection("users").document(userId).collection("canvases")

        val batch = db.batch()

        val existingDocs = collectionRef.get().await()
        for (doc in existingDocs) {
            batch.delete(doc.reference)
        }

        canvases.forEachIndexed { index, canvasData ->
            val dto = CanvasDataDTO(
                userId = userId,
                order = index,
                color = canvasData.color.value.toLong(),
                elements = canvasData.state.elements.map { el ->
                    TextElementDTO(el.id, el.text, el.xFraction, el.yFraction, el.fontSize, el.fontFamily, el.bold, el.italic)
                }
            )
            val docRef = collectionRef.document()
            batch.set(docRef, dto)
        }

        batch.commit().await()
    }
    suspend fun loadCanvases(): List<CanvasData> {
        val userId = getCurrentUserId() ?: return emptyList()

        val snapshot = db.collection("users")
            .document(userId)
            .collection("canvases")
            .orderBy("order")
            .get()
            .await()

        return snapshot.toObjects(CanvasDataDTO::class.java).map { dto ->
            val canvasState = CanvasState().apply {
                val textElements = dto.elements.map { el ->
                    TextElement(el.id, el.text, el.xFraction, el.yFraction, el.fontSize, el.fontFamily, el.bold, el.italic)
                }
                elements.addAll(textElements)
            }
            CanvasData(
                id = dto.order,
                state = canvasState,
                color = androidx.compose.ui.graphics.Color(dto.color.toULong())
            )
        }
    }
}