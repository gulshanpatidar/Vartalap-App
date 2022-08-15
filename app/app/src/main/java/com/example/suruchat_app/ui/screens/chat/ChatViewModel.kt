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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val isInternetAvailable: Boolean,
    val chatUseCases: ChatUseCases
) : ViewModel() {

    var messages: MutableState<List<Message>> = mutableStateOf(ArrayList())
    private val service = ChatService.create()
    val isLoading: MutableState<Boolean> = mutableStateOf(true)
    val errorMessage: MutableState<String> = mutableStateOf("")
    private val privateKeyString = GetToken.PRIVATE_KEY
    private val privateKeyBytes: ByteArray = Base64.decode(privateKeyString, Base64.DEFAULT)
    private val publicKey: String = GetToken.PUBLIC_KEY
    private val publicKeyBytes: ByteArray = Base64.decode(publicKey, Base64.DEFAULT)
    private var simpleImage = ""

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val imageUrl: MutableState<String> = mutableStateOf("")

//    init {
//        getMessagesInit()
//        viewModelScope.launch {
//            for (i in 1..1000){
//                delay(5000)
//                refresh()
//            }
//        }
//    }

    fun startGettingMessages(chatId: String){
        getMessagesInit(chatId)
        viewModelScope.launch {
            while(true){
                delay(5000)
                refresh(chatId)
            }
        }
    }

    fun getMessagesInit(chatId: String) {
        println("Public key - $publicKey")
        if (isInternetAvailable) {
            getMessagesOfflineThenOnline(chatId)
        } else {
            getMessagesOffline(chatId)
        }
    }

    fun refresh(chatId: String) {
        if (isInternetAvailable) {
            viewModelScope.launch {
//                _isRefreshing.emit(true)
                getMessages(chatId)
            }
        }
    }

    private fun getMessagesOffline(chatId: String) {
        viewModelScope.launch {
            messages.value = chatUseCases.getMessages(chatId).messages
            isLoading.value = false
        }
    }

    private fun getMessagesOfflineThenOnline(chatId: String) {
        viewModelScope.launch {
            messages.value = chatUseCases.getMessages(chatId).messages
            messages.value.forEach {
                Log.i("Testing", "getMessagesOfflineThenOnline: Message is - ${it.text}")
            }
            isLoading.value = false
            getMessages(chatId)
        }
    }

    private fun getMessages(chatId: String) {
        viewModelScope.launch {
            when (val result = service.getMessages(chatId)) {
                is Resource.Success -> {
                    val loadedMessages = result.data!!

                    loadedMessages.forEach {
                        if (GetToken.LOGIN_TIME!! < it.createdAt) {
                            if (it.text.isNotEmpty()){
                                val decryptedMessage =
                                    Curve25519Impl.decryptMessage(
                                        publicKeyBytes,
                                        privateKeyBytes,
                                        it.text
                                    )
                                it.text = decryptedMessage
                            }else{
                                val decryptedImage =
                                    Curve25519Impl.decryptMessage(publicKeyBytes,privateKeyBytes,it.image)
                                it.image = decryptedImage
                            }
                            it.chatId = chatId
                            chatUseCases.addMessage(it)
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
            _isRefreshing.emit(false)
        }
    }

    fun sendMessage(messageObject: SendMessageObject) {
        viewModelScope.launch {
            if (messageObject.text.isNotEmpty()){
                val simpleText = messageObject.text
                val encryptedMessage =
                    Curve25519Impl.encryptMessage(publicKeyBytes, privateKeyBytes, simpleText)
                messageObject.text = encryptedMessage
            }
            service.sendMessage(messageObject)
        }
    }

    fun uploadImage(chatId: String,fileName: String, file: File) {
        viewModelScope.launch {
            isLoading.value = true
            simpleImage = service.sendImage(fileName,file)
            val encryptedImage = Curve25519Impl.encryptMessage(publicKeyBytes,privateKeyBytes,simpleImage)
            val messageObject = SendMessageObject(chatId,System.currentTimeMillis(),"",encryptedImage)
            isLoading.value = false
            sendMessage(messageObject = messageObject)
        }
    }
}