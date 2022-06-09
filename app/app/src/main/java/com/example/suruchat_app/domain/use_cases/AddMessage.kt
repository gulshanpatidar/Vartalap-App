package com.example.suruchat_app.domain.use_cases

import com.example.suruchat_app.domain.models.Message
import com.example.suruchat_app.domain.repository.ChatRepository

class AddMessage(
    private val repository: ChatRepository
) {

    suspend operator fun invoke(message: Message){
        repository.insertMessage(message = message)
    }
}