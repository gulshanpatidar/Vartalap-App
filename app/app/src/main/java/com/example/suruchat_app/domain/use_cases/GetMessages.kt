package com.example.suruchat_app.domain.use_cases

import com.example.suruchat_app.domain.models.Message
import com.example.suruchat_app.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetMessages(
    private val repository: ChatRepository
) {

    operator fun invoke(): Flow<List<Message>>{
        return repository.getMessages()
    }
}