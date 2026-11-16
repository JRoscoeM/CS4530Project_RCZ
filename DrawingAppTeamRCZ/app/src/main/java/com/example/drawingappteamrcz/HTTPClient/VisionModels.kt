package com.example.drawingappteamrcz.HTTPClient

import kotlinx.serialization.Serializable

// ---------- Request ----------

@Serializable
data class AnnotateImagesRequest(
    val requests: List<AnnotateImageRequest>
)

@Serializable
data class AnnotateImageRequest(
    val image: VisionImage,
    val features: List<Feature>
)

/** [content] is the base64-encoded image bytes (PNG/JPEG). */
@Serializable
data class VisionImage(
    val content: String
)

@Serializable
data class Feature(
    val type: String = FeatureType.OBJECT_LOCALIZATION,
    val maxResults: Int = 10
)

object FeatureType
{
    const val OBJECT_LOCALIZATION = "OBJECT_LOCALIZATION"
    const val LABEL_DETECTION = "LABEL_DETECTION"
}

// ---------- Response ----------

@Serializable
data class AnnotateImagesResponse(
    val responses: List<AnnotateImageResponse> = emptyList()
)

@Serializable
data class AnnotateImageResponse(
    val localizedObjectAnnotations: List<LocalizedObjectAnnotation> = emptyList(),
    val labelAnnotations: List<EntityAnnotation> = emptyList(),
    val error: VisionStatus? = null
)

@Serializable
data class LocalizedObjectAnnotation(
    val mid: String? = null,
    val name: String,
    val score: Float,
    val boundingPoly: BoundingPoly
)

@Serializable
data class EntityAnnotation(
    val mid: String? = null,
    val description: String,
    val score: Float
)

/** For object localization, vertices are normalized to 0..1 relative to the image size. */
@Serializable
data class BoundingPoly(
    val normalizedVertices: List<NormalizedVertex> = emptyList()
)

/** Vision omits a coordinate when it is 0, hence the defaults. */
@Serializable
data class NormalizedVertex(
    val x: Float = 0f,
    val y: Float = 0f
)

@Serializable
data class VisionStatus(
    val code: Int? = null,
    val message: String? = null
)
