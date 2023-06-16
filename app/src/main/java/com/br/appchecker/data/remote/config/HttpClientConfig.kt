package com.br.appchecker.data.remote.config

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import java.util.concurrent.TimeUnit

object HttpClientConfig {
    val client: HttpClient = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = TimeUnit.SECONDS.toMillis(30)
        }
        defaultRequest {
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
    }
}