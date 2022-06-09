package com.example.suruchat_app.domain.use_cases

import com.example.suruchat_app.domain.models.Message
import com.example.suruchat_app.domain.models.UserChat
import com.example.suruchat_app.domain.models.UserChatWithMessage
import com.example.suruchat_app.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetMessages(
    private val repository: ChatRepository
) {

    suspend operator fun invoke(chatId: String): UserChatWithMessage{
        return repository.getMessagesByChatId(chatId)
    }
}