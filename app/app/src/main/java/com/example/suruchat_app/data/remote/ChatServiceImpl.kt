package com.example.suruchat_app.data.remote

import com.example.suruchat_app.data.remote.HttpRoutes.APP_USERS
import com.example.suruchat_app.data.remote.HttpRoutes.GET_MESSAGES
import com.example.suruchat_app.data.remote.HttpRoutes.LOGIN
import com.example.suruchat_app.data.remote.HttpRoutes.SEND_MESSAGE
import com.example.suruchat_app.data.remote.HttpRoutes.SIGNUP
import com.example.suruchat_app.data.remote.HttpRoutes.START_CHAT
import com.example.suruchat_app.data.remote.HttpRoutes.UPLOAD_IMAGE
import com.example.suruchat_app.data.remote.HttpRoutes.USER_CHATS
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.data.remote.dto.*
import com.example.suruchat_app.data.remote.util.Resource
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.util.*
import java.io.File

class ChatServiceImpl(
    private val client: HttpClient
) : ChatService {

    override suspend fun login(username: String, password: String): LoginResponse {
        val loginResponse = try {
            client.post<LoginResponse>(LOGIN) {
                contentType(ContentType.Application.Json)
                body = LoginObject(username = username, password = password)
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

        return loginResponse
    }

    override suspend fun signup(
        fullname: String,
        username: String,
        email: String,
        password: String
    ): String {
        val signupResponse = try {
            client.put<SignupResponse>(SIGNUP) {
                contentType(ContentType.Application.Json)
                body = SignupObject(
                    username = username,
                    email = email,
                    password = password,
                    fullname = fullname
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

        return signupResponse.message
    }

    override suspend fun getUserChats(): Resource<List<UserChat>> {
        val chatResponse = try {
            client.get<ChatResponse>(USER_CHATS)
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

        return Resource.Success(chatResponse.users)
    }

    override suspend fun getUsers(): Resource<List<User>> {
        val appUsers = try {
            client.get<AppUsers>(APP_USERS)
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

        return Resource.Success(appUsers.users)
    }

    override suspend fun startChat(userId: String): String {
        val startChatResponse = try {
            client.post<StartChatResponse>(START_CHAT) {
                contentType(ContentType.Application.Json)
                body = StartChatObject(user = userId)
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

        return startChatResponse.message
    }

    override suspend fun getMessages(chatId: String): Resource<List<Message>> {
        val messageResponse = try {
            client.get<MessageResponse>("$GET_MESSAGES/$chatId")
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

        return Resource.Success(messageResponse.messages)
    }

    override suspend fun sendMessage(sendMessageObject: SendMessageObject): String {
        val sendMessageResponse = try {
            client.post<SendMessageResponse>(SEND_MESSAGE) {
                contentType(ContentType.Application.Json)
                body = sendMessageObject
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

        return sendMessageResponse.Message
    }


    @InternalAPI
    override suspend fun uploadImage(fileName: String, image: File): String {
        val imageResponse = try {
            client.submitFormWithBinaryData<ImageResponse>(url = UPLOAD_IMAGE, formData = formData{
                append("image",image.readBytes(),Headers.build {
                    append(HttpHeaders.ContentType,"image/png")
                    append(HttpHeaders.ContentDisposition,"filename=${image.name}")
                })
            }) {
                onUpload{ bytesSentTotal, contentLength ->
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

        return imageResponse.imageurl
    }
}