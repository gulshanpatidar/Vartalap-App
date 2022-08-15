package com.example.suruchat_app.data.remote

import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.data.remote.HttpRoutes.APP_USERS
import com.example.suruchat_app.data.remote.HttpRoutes.GET_MESSAGES
import com.example.suruchat_app.data.remote.HttpRoutes.LOGIN
import com.example.suruchat_app.data.remote.HttpRoutes.SEND_IMAGE
import com.example.suruchat_app.data.remote.HttpRoutes.SEND_MESSAGE
import com.example.suruchat_app.data.remote.HttpRoutes.SIGNUP
import com.example.suruchat_app.data.remote.HttpRoutes.START_CHAT
import com.example.suruchat_app.data.remote.HttpRoutes.UPDATE_PROFILE
import com.example.suruchat_app.data.remote.HttpRoutes.UPLOAD_IMAGE
import com.example.suruchat_app.data.remote.HttpRoutes.USER_CHATS
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.domain.util.Resource
import com.example.suruchat_app.domain.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.util.*
import java.io.File

class ChatServiceImpl(
    private val client: HttpClient
) : ChatService {

    override suspend fun login(
        username: String,
        password: String,
        publicKey: String
    ): LoginResponse {
        val response = try {
            client.post(LOGIN) {
                contentType(ContentType.Application.Json)
                setBody(LoginObject(username = username, password = password, pubkey = publicKey))
            }
        } catch (e: RedirectResponseException) {
            // 3XX responses
            println("Error: ${e.response.status.description}")
            return LoginResponse(token = e.response.status.description, "", "", "")
        } catch (e: ClientRequestException) {
            // 4XX responses
            println("Error: ${e.response.status.description}")
            return LoginResponse(e.response.status.description, "", "", "")
        } catch (e: ServerResponseException) {
            // 5XX responses
            println("Error: ${e.response.status.description}")
            return LoginResponse(e.response.status.description, "", "", "")
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return LoginResponse(e.message.toString(), "", "", "")
        }
        return response.body()
    }

    override suspend fun signup(
        fullname: String,
        username: String,
        email: String,
        password: String
    ): String {
        val signupResponse = try {
            client.post(SIGNUP) {
                contentType(ContentType.Application.Json)
                setBody(
                    SignupObject(
                        username = username,
                        email = email,
                        password = password,
                        fullname = fullname
                    )
                )
            }
        } catch (e: RedirectResponseException) {
            // 3XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: ClientRequestException) {
            // 4XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: ServerResponseException) {
            // 5XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return e.message.toString()
        }

        return signupResponse.body<SignupResponse>().message
    }

    override suspend fun getUserChats(): Resource<List<UserChat>> {
        val response = try {
            client.get(USER_CHATS) {
                headers {
                    append("Token", GetToken.ACCESS_TOKEN.toString())
                }
            }
        } catch (e: RedirectResponseException) {
            // 3XX responses
            println("Error: ${e.response.status.description}")
            return Resource.Error(e.response.status.description)
        } catch (e: ClientRequestException) {
            // 4XX responses
            println("Error: ${e.response.status.description}")
            return Resource.Error(e.response.status.description)
        } catch (e: ServerResponseException) {
            // 5XX responses
            println("Error: ${e.response.status.description}")
            return Resource.Error(e.response.status.description)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return Resource.Error(e.message.toString())
        }
        val chatResponse = response.body<ChatResponse>()
        return Resource.Success(chatResponse.users)
    }

    override suspend fun getUsers(): Resource<List<User>> {
        val response = try {
            client.get(APP_USERS) {
                headers {
                    append("Token", GetToken.ACCESS_TOKEN.toString())
                }
            }
        } catch (e: RedirectResponseException) {
            // 3XX responses
            println("Error: ${e.response.status.description}")
            return Resource.Error(e.response.status.description)
        } catch (e: ClientRequestException) {
            // 4XX responses
            println("Error: ${e.response.status.description}")
            return Resource.Error(e.response.status.description)
        } catch (e: ServerResponseException) {
            // 5XX responses
            println("Error: ${e.response.status.description}")
            return Resource.Error(e.response.status.description)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return Resource.Error(e.message.toString())
        }
        val appUsers = response.body<AppUsers>()
        return Resource.Success(appUsers.users)
    }

    override suspend fun startChat(userId: String): String {
        val response = try {
            println(userId)
            client.post(START_CHAT) {
                contentType(ContentType.Application.Json)
                setBody(StartChatObject(userId = userId))
                headers {
                    append("Token", GetToken.ACCESS_TOKEN.toString())
                }
            }
        } catch (e: RedirectResponseException) {
            // 3XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: ClientRequestException) {
            // 4XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: ServerResponseException) {
            // 5XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return e.message.toString()
        }
        println("ye dekho daya - ${response.contentType()}")
        return response.body<StartChatResponse>().message
    }

    override suspend fun getMessages(chatId: String): Resource<List<Message>> {
        val response = try {
            client.get("$GET_MESSAGES/$chatId") {
                headers {
                    append("Token", GetToken.ACCESS_TOKEN.toString())
                }
            }
        } catch (e: RedirectResponseException) {
            // 3XX responses
            println("Error: ${e.response.status.description}")
            return Resource.Error(e.response.status.description)
        } catch (e: ClientRequestException) {
            // 4XX responses
            println("Error: ${e.response.status.description}")
            return Resource.Error(e.response.status.description)
        } catch (e: ServerResponseException) {
            // 5XX responses
            println("Error: ${e.response.status.description}")
            return Resource.Error(e.response.status.description)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return Resource.Error(e.message.toString())
        }
        val messageResponse = response.body<MessageResponse>()
        return Resource.Success(messageResponse.messages)
    }

    override suspend fun sendMessage(sendMessageObject: SendMessageObject): String {
        val response = try {
            client.post(SEND_MESSAGE) {
                headers {
                    append("Token", GetToken.ACCESS_TOKEN.toString())
                }
                contentType(ContentType.Application.Json)
                setBody(sendMessageObject)
            }
        } catch (e: RedirectResponseException) {
            // 3XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: ClientRequestException) {
            // 4XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: ServerResponseException) {
            // 5XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return e.message.toString()
        }
        val sendMessageResponse = response.body<SendMessageResponse>()
        return sendMessageResponse.message
    }


    @InternalAPI
    override suspend fun uploadImage(fileName: String, image: File): String {
        val response = try {
            client.submitFormWithBinaryData(url = UPLOAD_IMAGE, formData = formData {
                append("image", image.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, "filename=${image.name}")
                    append("Token", GetToken.ACCESS_TOKEN.toString())
                })
            }) {
                onUpload { bytesSentTotal, contentLength ->
                    println("sent $bytesSentTotal bytes from $contentLength")
                }
            }
        } catch (e: RedirectResponseException) {
            // 3XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: ClientRequestException) {
            // 4XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: ServerResponseException) {
            // 5XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return e.message.toString()
        }
        val imageResponse = response.body<ImageResponse>()
        return imageResponse.imageurl
    }

    @InternalAPI
    override suspend fun sendImage(filename: String, image: File): String {
        val response = try {
            client.submitFormWithBinaryData(
                url = SEND_IMAGE,
                formData = formData {
                    append("image", image.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, "image/png")
                        append(HttpHeaders.ContentDisposition, "filename=${image.name}")
                        append("Token", GetToken.ACCESS_TOKEN.toString())
                    })
                }) {
                onUpload { bytesSentTotal, contentLength ->
                    println("sent $bytesSentTotal bytes from $contentLength")
                }
            }
        } catch (e: RedirectResponseException) {
            // 3XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: ClientRequestException) {
            // 4XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: ServerResponseException) {
            // 5XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return e.message.toString()
        }

        return response.body<ImageResponse>().imageurl
    }

    override suspend fun updateProfile(fullname: String): String {
        val httpResponse = try {
            client.post(UPDATE_PROFILE) {
                headers {
                    append("Token",GetToken.ACCESS_TOKEN.toString())
                }
                contentType(ContentType.Application.Json)
                setBody(fullname)
            }
        } catch (e: RedirectResponseException) {
            // 3XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: ClientRequestException) {
            // 4XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: ServerResponseException) {
            // 5XX responses
            println("Error: ${e.response.status.description}")
            return e.response.status.description
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return e.message.toString()
        }

        return httpResponse.body()
    }
}