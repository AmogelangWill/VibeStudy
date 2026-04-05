package com.example.myapplication.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File
import java.io.FileOutputStream

object PdfPageRenderer {

    /**
     * Renders a specific page from a PDF in the assets folder
     * @param context Android context
     * @param assetPdfPath Path to PDF in assets folder
     * @param pageIndex Zero-based page index (0 for first page)
     * @return ImageBitmap of the rendered page, or null if rendering fails
     */
    fun renderPage(context: Context, assetPdfPath: String, pageIndex: Int): ImageBitmap? {
        return try {
            // Copy PDF from assets to cache directory (PdfRenderer needs a file descriptor)
            val cacheFile = File(context.cacheDir, "temp_${assetPdfPath.hashCode()}.pdf")

            if (!cacheFile.exists()) {
                context.assets.open(assetPdfPath).use { input ->
                    FileOutputStream(cacheFile).use { output ->
                        input.copyTo(output)
                    }
                }
            }

            // Open the PDF
            val fileDescriptor = ParcelFileDescriptor.open(
                cacheFile,
                ParcelFileDescriptor.MODE_READ_ONLY
            )

            val pdfRenderer = PdfRenderer(fileDescriptor)

            // Check if page index is valid
            if (pageIndex < 0 || pageIndex >= pdfRenderer.pageCount) {
                pdfRenderer.close()
                fileDescriptor.close()
                return null
            }

            // Open the specific page
            val page = pdfRenderer.openPage(pageIndex)

            // Create bitmap with appropriate dimensions
            // Scale up for better quality
            val scale = 2.0f
            val bitmap = Bitmap.createBitmap(
                (page.width * scale).toInt(),
                (page.height * scale).toInt(),
                Bitmap.Config.ARGB_8888
            )

            // Render page to bitmap
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            // Convert to ImageBitmap for Compose
            val imageBitmap = bitmap.asImageBitmap()

            // Clean up
            page.close()
            pdfRenderer.close()
            fileDescriptor.close()

            imageBitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Renders multiple pages from a PDF
     */
    fun renderPages(context: Context, assetPdfPath: String, pageIndices: List<Int>): List<ImageBitmap> {
        return pageIndices.mapNotNull { pageIndex ->
            renderPage(context, assetPdfPath, pageIndex)
        }
    }

    /**
     * Clears cached PDF files
     */
    fun clearCache(context: Context) {
        context.cacheDir.listFiles()?.forEach { file ->
            if (file.name.startsWith("temp_") && file.name.endsWith(".pdf")) {
                file.delete()
            }
        }
    }
}

