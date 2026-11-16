package com.example.drawingappteamrcz.HTTPClient

import android.util.Log
import com.example.drawingappteamrcz.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Single shared Ktor [HttpClient] used to talk to the Google Cloud Vision API.
 *
 * HttpClient is expensive to create and is thread-safe, so the app should use one instance.
 */
object Http
{
    private const val TAG = "Http"

    val json = Json {
        ignoreUnknownKeys = true   // Vision responses contain many fields we don't model
        explicitNulls = false      // don't send `"field": null` in requests
        encodeDefaults = true      // always send defaults like `type` / `maxResults`
    }

    val client: HttpClient by lazy {
        HttpClient(Android) {
            expectSuccess = true   // non-2xx responses throw ResponseException

            install(ContentNegotiation) {
                json(json)
            }

            install(HttpTimeout) {
                connectTimeoutMillis = 15_000
                requestTimeoutMillis = 30_000   // image uploads can be large
                socketTimeoutMillis = 30_000
            }

            install(Logging) {
                logger = object : Logger
                {
                    override fun log(message: String)
                    {
                        Log.d(TAG, message)
                    }
                }
                // HEADERS only: BODY would dump base64 image payloads into logcat
                level = if (BuildConfig.DEBUG) LogLevel.HEADERS else LogLevel.NONE
                sanitizeHeader { it.equals(Routing.API_KEY_HEADER, ignoreCase = true) }
            }

            defaultRequest {
                url(Routing.VISION_BASE_URL)
                contentType(ContentType.Application.Json)
                headers.append(Routing.API_KEY_HEADER, BuildConfig.VISION_API_KEY)
            }
        }
    }
}
