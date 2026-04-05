package com.example.myapplication.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.PaperManifestLoader
import com.example.myapplication.data.QuestionEntry
import com.example.myapplication.data.PaperManifest
import com.example.myapplication.ui.theme.CardBackground
import com.example.myapplication.ui.theme.PrimaryButton
import com.example.myapplication.ui.theme.TextColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PaperViewScreen(manifestAsset: String) {
    val context = LocalContext.current
    val manifest = remember(manifestAsset) { PaperManifestLoader.load(context, manifestAsset) }

    if (manifest == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Failed to load paper manifest", color = Color.Red, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Text(text = "File: $manifestAsset", color = Color.Gray, fontSize = 12.sp)
            }
        }
        return
    }

    // Load the corresponding memo manifest
    val memoManifestAsset = manifestAsset.replace(".json", "_MEMO.json")
    val memoManifest = remember(memoManifestAsset) {
        PaperManifestLoader.load(context, memoManifestAsset)
    }

    Column(Modifier.fillMaxSize()) {
        Text(
            text = "${manifest.subject.replace('_',' ')} Paper ${manifest.paper} - ${manifest.year} ${manifest.month}",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(16.dp),
            color = TextColor
        )

        LazyColumn(Modifier.fillMaxSize()) {
            // Sort questions by number (Q1, Q2, etc.)
            val sortedQuestions = manifest.questions.entries.sortedBy {
                it.key.removePrefix("Q").toIntOrNull() ?: 0
            }

            items(sortedQuestions) { (questionId, questionEntry) ->
                QuestionCard(
                    questionId = questionId,
                    questionEntry = questionEntry,
                    memoEntry = memoManifest?.questions?.get(questionId),
                    manifest = manifest,
                    memoManifest = memoManifest
                )
            }
            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}

@Composable
private fun QuestionCard(
    questionId: String,
    questionEntry: QuestionEntry,
    memoEntry: QuestionEntry?,
    manifest: PaperManifest,
    memoManifest: PaperManifest?
) {
    var showMemo by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Load question images
    var questionImages by remember { mutableStateOf<List<ImageBitmap>?>(null) }
    var memoImages by remember { mutableStateOf<List<ImageBitmap>?>(null) }
    var isLoadingQuestions by remember { mutableStateOf(true) }
    var isLoadingMemos by remember { mutableStateOf(false) }

    // Determine the asset folder name from the manifest
    val questionFolder = remember(manifest) {
        val subject = manifest.subject.replace('_', ' ')
        val year = manifest.year
        val month = manifest.month.substring(0, 3) // November -> Nov
        val paper = manifest.paper

        "$subject $paper $month $year Eng"
    }

    val memoFolder = remember(memoManifest) {
        memoManifest?.let {
            val subject = it.subject.replace('_', ' ')
            val year = it.year
            val month = it.month.substring(0, 3)
            val paper = it.paper
            "$subject $paper $month $year MEMO"
        }
    }

    // Load question images from assets
    LaunchedEffect(questionEntry) {
        isLoadingQuestions = true
        questionImages = withContext(Dispatchers.IO) {
            questionEntry.images.mapNotNull { imageName ->
                try {
                    context.assets.open("$questionFolder/$imageName").use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                    }
                } catch (e: Exception) {
                    null
                }
            }
        }
        isLoadingQuestions = false
    }

    // Load memo images when shown
    LaunchedEffect(showMemo) {
        if (showMemo && memoImages == null && memoEntry != null && memoFolder != null) {
            isLoadingMemos = true
            memoImages = withContext(Dispatchers.IO) {
                memoEntry.images.mapNotNull { imageName ->
                    try {
                        context.assets.open("$memoFolder/$imageName").use { inputStream ->
                            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                        }
                    } catch (e: Exception) {
                        null
                    }
                }
            }
            isLoadingMemos = false
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            // Question title
            Text(
                text = questionId,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Question images
            if (isLoadingQuestions) {
                Box(
                    Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                questionImages?.forEach { bitmap ->
                    Image(
                        bitmap = bitmap,
                        contentDescription = "$questionId image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { /* TODO: Ask Tutor */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryButton),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Ask Tutor", color = Color.White)
                }

                Button(
                    onClick = { showMemo = !showMemo },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (showMemo) PrimaryButton.copy(alpha = 0.7f) else PrimaryButton
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (showMemo) "Hide Memo" else "View Memo", color = Color.White)
                }
            }

            // Memo images
            if (showMemo) {
                Spacer(Modifier.height(16.dp))

                if (isLoadingMemos) {
                    Box(
                        Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (memoImages != null) {
                    Text(
                        text = "Memorandum",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    memoImages?.forEach { bitmap ->
                        Image(
                            bitmap = bitmap,
                            contentDescription = "$questionId memo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                } else {
                    Text(
                        text = "Memo not available",
                        color = TextColor.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
