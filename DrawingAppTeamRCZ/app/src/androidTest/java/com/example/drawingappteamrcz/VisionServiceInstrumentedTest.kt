package com.example.drawingappteamrcz.HTTPClient

import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class VisionServiceInstrumentedTest {

    @Test
    fun detectObjects_postsToVisionEndpoint_andParsesDisplayData() = runBlocking {
        val client = testClient { request ->
            assertEquals(HttpMethod.Post, request.method)
            assertEquals("vision.googleapis.com", request.url.host)
            assertEquals("/v1/images:annotate", request.url.encodedPath)

            respond(
                content = """
                    {
                      "responses": [
                        {
                          "localizedObjectAnnotations": [
                            {
                              "name": "Cat",
                              "score": 0.91,
                              "boundingPoly": {
                                "normalizedVertices": [
                                  { "x": 0.1, "y": 0.2 },
                                  { "x": 0.8, "y": 0.2 },
                                  { "x": 0.8, "y": 0.9 },
                                  { "x": 0.1, "y": 0.9 }
                                ]
                              }
                            }
                          ]
                        }
                      ]
                    }
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }

        val result = VisionService(client).detectObjects(testBitmap())

        assertTrue(result.isSuccess)

        val detectedObject = result.getOrThrow().single()
        assertEquals("Cat", detectedObject.name)
        assertEquals(0.91f, detectedObject.score, 0.001f)
        assertEquals(4, detectedObject.boundingPoly.normalizedVertices.size)
        assertEquals(
            0.1f,
            detectedObject.boundingPoly.normalizedVertices.first().x,
            0.001f
        )

        client.close()
    }

    @Test
    fun detectObjects_returnsEmptyList_whenNoObjectsAreDetected() = runBlocking {
        val client = testClient {
            respond(
                content = """{ "responses": [ {} ] }""",
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }

        val result = VisionService(client).detectObjects(testBitmap())

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())

        client.close()
    }

    @Test
    fun detectObjects_returnsFailure_whenNetworkRequestFails() = runBlocking {
        val client = testClient {
            throw IOException("Network unavailable")
        }

        val result = VisionService(client).detectObjects(testBitmap())

        assertTrue(result.isFailure)
        assertEquals("Network unavailable", result.exceptionOrNull()?.message)

        client.close()
    }

    private fun testClient(
        handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData
    ): HttpClient {
        return HttpClient(MockEngine { request -> handler(request) }) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    private fun testBitmap(): Bitmap {
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    }
}