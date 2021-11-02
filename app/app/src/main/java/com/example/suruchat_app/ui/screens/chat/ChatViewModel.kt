package com.example.suruchat_app.ui.screens.chat

import android.util.Base64
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.domain.models.Message
import com.example.suruchat_app.domain.models.SendMessageObject
import com.example.suruchat_app.domain.use_cases.ChatUseCases
import com.example.suruchat_app.domain.util.Resource
import com.example.suruchat_app.security.Curve25519Impl
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ChatViewModel(
    val isInternetAvailable: Boolean,
    val chatUseCases: ChatUseCases,
    private val chatId: String
) : ViewModel() {

    var messages: MutableState<List<Message>> = mutableStateOf(ArrayList())
    private val service = ChatService.create()
    val isLoading: MutableState<Boolean> = mutableStateOf(true)
    val errorMessage: MutableState<String> = mutableStateOf("")
    private val privateKeyString = GetToken.PRIVATE_KEY
    private val privateKeyBytes: ByteArray = Base64.decode(privateKeyString, Base64.DEFAULT)
    private val publicKey: String = GetToken.PUBLIC_KEY
    private val publicKeyBytes: ByteArray = Base64.decode(publicKey, Base64.DEFAULT)

    private var getMessagesJob: Job? = null

    init {
//        println("Public key - $publicKey")
//        if (isInternetAvailable) {
//            getMessagesOfflineThenOnline(chatId)
//        } else {
//            getMessagesOffline(chatId)
//        }
        getMessages(chatId)
    }

    fun getMessagesOffline(chatId: String) {
        viewModelScope.launch {
            messages.value = chatUseCases.getMessages(chatId).messages
            isLoading.value = false
        }
    }

    fun getMessagesOfflineThenOnline(chatId: String) {
        viewModelScope.launch {
            messages.value = chatUseCases.getMessages(chatId).messages
            messages.value.forEach {
                Log.i("Testing", "getMessagesOfflineThenOnline: Message is - ${it.text}")
            }
            isLoading.value = false
        }
        getMessages(chatId)
    }

    private fun getMessages(chatId: String) {
        viewModelScope.launch {
            when (val result = service.getMessages(chatId)) {
                is Resource.Success -> {
//                    val loadedMessages = result.data!!
//                    loadedMessages.forEach {
//                        it.chatId = chatId
//                        chatUseCases.addMessage(it)
//                    }
                    val loadedMessages = result.data!!

                    loadedMessages.forEach {
                        if (GetToken.LOGIN_TIME!!<it.createdAt){
                            val decryptedMessage =
                                Curve25519Impl.decryptMessage(publicKeyBytes, privateKeyBytes, it.text)
                            it.text = decryptedMessage
//                            it.chatId = chatId
//                            chatUseCases.addMessage(it)
                        }
                    }
                    messages.value = loadedMessages
                    isLoading.value = false
                }
                is Resource.Error -> {
                    errorMessage.value = result.message.toString()
                    isLoading.value = false
                }
                else -> {

                }
            }
        }
    }

    fun sendMessage(messageObject: SendMessageObject) {
        viewModelScope.launch {
            val simpleText = messageObject.text
            val encryptedMessage =
                Curve25519Impl.encryptMessage(publicKeyBytes, privateKeyBytes, simpleText)
            messageObject.text = encryptedMessage
            service.sendMessage(messageObject)
        }
    }
}