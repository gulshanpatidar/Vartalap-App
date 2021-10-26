package com.example.suruchat_app.data.remote

import com.example.suruchat_app.data.remote.HttpRoutes.BASE_URL
import com.example.suruchat_app.data.remote.HttpRoutes.LOGIN
import com.example.suruchat_app.data.remote.HttpRoutes.SIGNUP
import com.example.suruchat_app.data.remote.HttpRoutes.USER_CHATS
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.data.remote.dto.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.lang.Exception

class ChatServiceImpl(
    private val client: HttpClient
) : ChatService {

    override suspend fun login(username: String, password: String) : LoginResponse{
        val loginResponse = try {
            client.post<LoginResponse>(LOGIN) {
                contentType(ContentType.Application.Json)
                body = LoginObject(username = username,password = password)
            }
        } catch (e: RedirectResponseException) {
            // 3XX responses
            println("Error: ${e.response.status.description}")
            return LoginResponse(e.response.status.description,"")
        } catch (e: ClientRequestException) {
            // 4XX responses
            println("Error: ${e.response.status.description}")
            return LoginResponse(e.response.status.description,"")
        } catch (e: ServerResponseException) {
            // 5XX responses
            println("Error: ${e.response.status.description}")
            return LoginResponse(e.response.status.description,"")
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return LoginResponse(e.message.toString(),"")
        }

        return loginResponse
    }

    override suspend fun signup(username: String, email: String, password: String): String {
        val signupResponse = try {
            client.put<SignupResponse>(SIGNUP){
                contentType(ContentType.Application.Json)
                body = SignupObject(username = username,email = email,password = password)
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

    override suspend fun getUserChats(): List<UserChat> {
        val chatResponse = try {
            client.get<ChatResponse>(USER_CHATS)
        } catch (e: RedirectResponseException) {
            // 3XX responses
            println("Error: ${e.response.status.description}")
            return emptyList()
        } catch (e: ClientRequestException) {
            // 4XX responses
            println("Error: ${e.response.status.description}")
            return emptyList()
        } catch (e: ServerResponseException) {
            // 5XX responses
            println("Error: ${e.response.status.description}")
            return emptyList()
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return emptyList()
        }

        return chatResponse.users
    }
}