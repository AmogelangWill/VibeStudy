package com.example.myapplication.data

import android.content.Context
import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
@Immutable
data class PaperManifest(
    val grade: String,
    val subject: String,
    val year: String,
    val month: String,
    val paper: String,
    val type: String,
    val questions: Map<String, QuestionEntry>
)

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
@Immutable
data class QuestionEntry(
    val images: List<String>
)

object PaperManifestLoader {
    private val json = Json { ignoreUnknownKeys = true }

    fun load(context: Context, assetName: String): PaperManifest? = runCatching<PaperManifest?> {
        context.assets.open(assetName).bufferedReader().use { reader ->
            json.decodeFromString(reader.readText())
        }
    }.getOrElse { null }
}
