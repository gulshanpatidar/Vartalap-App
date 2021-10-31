package com.example.suruchat_app.domain.use_cases

import com.example.suruchat_app.domain.models.UserChat
import com.example.suruchat_app.domain.repository.ChatRepository

class AddUserChat(
    private val repository: ChatRepository
) {

    suspend operator fun invoke(userChat: UserChat){
        repository.insertUserChat(userChat)
    }
}