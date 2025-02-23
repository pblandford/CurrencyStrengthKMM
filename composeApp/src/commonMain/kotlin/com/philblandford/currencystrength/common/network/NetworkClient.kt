package com.philblandford.currencystrength.common.network

import com.philblandford.currencystrength.common.log.log
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.util.flatMap
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.websocket.Frame
import io.ktor.websocket.readReason
import io.ktor.websocket.readText
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

const val WEBSOCKET_URL = "192.168.0.33:5000"
const val BASE_URL = "http://192.168.0.33:5000"
//const val WEBSOCKET_URL = "10.0.2.2:5000"
//const val BASE_URL = "http://10.0.2.2:5000"
//const val WEBSOCKET_URL = "currencystrength.duckdns.org"
//const val BASE_URL = "https://currencystrength.duckdns.org"

open class NetworkClient {
    val jsonConfig = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    val httpClient = HttpClient {
        install(ContentNegotiation) {

        }
        install(WebSockets) {
            pingIntervalMillis = 10000
        }
    }

    suspend inline fun getString(path: String): Result<String> {
        return httpClient.get("$BASE_URL/$path").toResult()
    }

    suspend inline fun <reified T> getJson(
        path: String,
        params: List<Pair<String, String>> = listOf()
    ): Result<T> {
        return httpClient.get("$BASE_URL/$path") {
            contentType(ContentType.Application.Json)
            params.forEach { parameter(it.first, it.second) }
        }.toResultJson()
    }

    suspend inline fun <reified R> post(
        path: String, parameters: Map<String, String> = mapOf()
    ): Result<String> {
        return runCatching {
            httpClient.submitForm("$BASE_URL/$path",
                formParameters = Parameters.build {
                    parameters.forEach { (key, value) ->
                        append(key, value)
                    }
                }
            ) {}
        }.flatMap { it.toResult() }
    }

    suspend inline fun HttpResponse.toResult(): Result<String> {
        if (this.status.value in 200..299) {
            return Result.success(this.body<String>())
        } else {
            return Result.failure(Exception("Failed to fetch data: ${this.status.value} ${this.bodyAsText()}"))
        }
    }

    suspend inline fun <reified T> HttpResponse.toResultJson(): Result<T> {
        if (this.status.value in 200..299) {
            val json = this.bodyAsText()
            return Result.success(jsonConfig.decodeFromString(json))
        } else {
            val json = this.bodyAsText()
            return Result.failure(Exception("Failed to fetch data: ${this.status.value} $json}"))
        }
    }

    suspend fun websocket(regid: String, onReceive: suspend (Alert) -> Unit) {
        log("Websocket $WEBSOCKET_URL")
        httpClient.webSocket(
            method = HttpMethod.Get,
            host = WEBSOCKET_URL, path = "/ws/$regid"
        ) {
            val session = this
            val receiveJob = launch {
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            val message = frame.readText()
                            println("Received: $message")
                            val alert = jsonConfig.decodeFromString<Alert>(message)
                            onReceive(alert)
                        }

                        is Frame.Ping -> {
                            send(Frame.Pong(frame.data)) // Respond to keep alive
                        }

                        is Frame.Close -> {
                            println("WebSocket closing: ${frame.readReason()}")
                            return@launch
                        }

                        else -> {} // Ignore other frames
                    }
                }
            }

            receiveJob.join()
        }
    }

}
