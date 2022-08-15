package com.example.suruchat_app.data.remote.api

import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.data.remote.ChatServiceImpl
import com.example.suruchat_app.domain.util.Resource
import com.example.suruchat_app.domain.models.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.kotlinx.serializer.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.io.File
import java.security.PublicKey

interface ChatService {

    suspend fun login(username: String,password: String,publicKey: String): LoginResponse

    suspend fun signup(fullname: String,username: String,email: String, password: String): String

    suspend fun getUserChats(): Resource<List<UserChat>>

    suspend fun getUsers(): Resource<List<User>>

    suspend fun startChat(userId: String): String

    suspend fun getMessages(chatId: String): Resource<List<Message>>

    suspend fun sendMessage(sendMessageObject: SendMessageObject): String

    suspend fun uploadImage(fileName: String, image: File): String

    suspend fun sendImage(filename: String,image: File): String

    suspend fun updateProfile(fullname: String): String

    companion object{
        fun create(): ChatService{
            return ChatServiceImpl(
                client = HttpClient(CIO){
                    install(Logging){
                        level = LogLevel.ALL
                    }
                    install(ContentNegotiation){
                        json(Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        })
                    }
//                    install(Auth){
//                        bearer {
//                            loadTokens {
//                                BearerTokens(
//                                    accessToken = GetToken.ACCESS_TOKEN.toString(),
//                                    refreshToken = ""
//                                )
//                            }
//                        }
//                    }
                }
            )
        }
    }
}