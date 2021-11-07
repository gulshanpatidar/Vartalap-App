package com.example.suruchat_app.data.remote.api

import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.data.remote.ChatServiceImpl
import com.example.suruchat_app.domain.util.Resource
import com.example.suruchat_app.domain.models.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
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
                client = HttpClient(Android){
                    install(Logging){
                        level = LogLevel.ALL
                    }
                    install(JsonFeature){
                        serializer = KotlinxSerializer()
                    }
                    install(Auth){
                        bearer {
                            loadTokens {
                                BearerTokens(
                                    accessToken = GetToken.ACCESS_TOKEN.toString(),
                                    refreshToken = ""
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}