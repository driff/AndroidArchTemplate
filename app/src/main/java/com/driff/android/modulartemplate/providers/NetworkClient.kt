package com.driff.android.modulartemplate.providers

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Inject

class NetworkClient @Inject constructor() {

    operator fun invoke() = HttpClient(OkHttp) {
        expectSuccess = false
        install(Logging)
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("TAG_KTOR_LOGGER", message)
                }

            }
            level = LogLevel.ALL
        }
        install(ResponseObserver) {
            onResponse { response ->
                Log.d("TAG_HTTP_STATUS_LOGGER", "${response.status.value}")
            }
        }

        engine {
            config {
                followSslRedirects(true)
            }
        }
    }

}