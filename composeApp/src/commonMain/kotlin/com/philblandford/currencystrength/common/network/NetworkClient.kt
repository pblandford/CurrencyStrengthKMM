package com.philblandford.currencystrength.common.network

import com.philblandford.currencystrength.common.util.flatMap
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

//const val BASE_URL = "http://192.168.0.33:5000"
//const val BASE_URL = "http://10.0.2.2:5000"
const val BASE_URL = "https://currencystrength.duckdns.org"

open class NetworkClient {
    val jsonConfig = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    val httpClient = HttpClient {
        install(ContentNegotiation) {

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

    suspend inline fun <reified T>HttpResponse.toResultJson():Result<T> {
        if (this.status.value in 200..299) {
            val json = this.bodyAsText()
            return Result.success(jsonConfig.decodeFromString(json))
        } else {
            val json = this.bodyAsText()
            return Result.failure(Exception("Failed to fetch data: ${this.status.value} $json}"))
        }
    }
}
