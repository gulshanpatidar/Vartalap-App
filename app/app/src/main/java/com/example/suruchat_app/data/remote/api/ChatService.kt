package com.example.suruchat_app.data.remote.api

import com.example.suruchat_app.data.remote.ChatServiceImpl
import com.example.suruchat_app.data.remote.dto.LoginResponse
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

interface ChatService {

    suspend fun login(username: String,password: String): LoginResponse

    suspend fun signup(username: String,email: String, password: String): String

    suspend fun getChatResponse(): String

    companion object{
        fun create(): ChatService{
            return ChatServiceImpl(
                client = HttpClient(Android){
                    install(Logging){
                        level = LogLevel.ALL
                    }
                    install(JsonFeature){
                        serializer = KotlinxSerializer()
                    }
                }
            )
        }
    }
}