package com.example.drawingappteamrcz.HTTPClient

/**
 * Endpoint definitions for the Google Cloud Vision REST API.
 * Docs: https://cloud.google.com/vision/docs/reference/rest/v1/images/annotate
 */
object Routing
{
    /** Trailing slash matters: relative paths below are resolved against this. */
    const val VISION_BASE_URL = "https://vision.googleapis.com/v1/"

    /** POST — runs one or more features (e.g. OBJECT_LOCALIZATION) on a batch of images. */
    const val ANNOTATE_IMAGES = "images:annotate"

    /** Google APIs accept the key as a header, which keeps it out of URLs and logs. */
    const val API_KEY_HEADER = "X-Goog-Api-Key"
}
