package com.example.drawingappteamrcz.HTTPClient

import android.graphics.Bitmap
import android.util.Base64
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

/**
 * Thin wrapper around the Cloud Vision `images:annotate` endpoint.
 * Callers (Repository / ViewModel) get domain-friendly results and never touch Ktor directly.
 * The client is injectable so tests can pass an HttpClient backed by Ktor's MockEngine.
 */
class VisionService(
    private val client: HttpClient = Http.client
)
{
    /** Runs OBJECT_LOCALIZATION on [bitmap] and returns the detected objects. */
    suspend fun detectObjects(
        bitmap: Bitmap,
        maxResults: Int = 10
    ): Result<List<LocalizedObjectAnnotation>> = runCatching {
        val request = AnnotateImagesRequest(
            requests = listOf(
                AnnotateImageRequest(
                    image = VisionImage(content = bitmap.toBase64Png()),
                    features = listOf(Feature(FeatureType.OBJECT_LOCALIZATION, maxResults))
                )
            )
        )

        val response: AnnotateImagesResponse = client.post(Routing.ANNOTATE_IMAGES) {
            setBody(request)
        }.body()

        val first = response.responses.firstOrNull()
            ?: error("Vision API returned no responses")
        first.error?.let { error("Vision API error ${it.code}: ${it.message}") }
        first.localizedObjectAnnotations
    }
}

/** PNG-compress and base64-encode a bitmap off the main thread. */
suspend fun Bitmap.toBase64Png(): String = withContext(Dispatchers.Default) {
    val bytes = ByteArrayOutputStream().use { out ->
        compress(Bitmap.CompressFormat.PNG, 100, out)
        out.toByteArray()
    }
    // android.util.Base64 because java.util.Base64 requires API 26 (minSdk is 24)
    Base64.encodeToString(bytes, Base64.NO_WRAP)
}
