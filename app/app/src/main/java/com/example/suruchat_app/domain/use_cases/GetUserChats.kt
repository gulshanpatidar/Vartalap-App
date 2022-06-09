package com.example.suruchat_app.domain.use_cases

import com.example.suruchat_app.domain.models.UserChat
import com.example.suruchat_app.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetUserChats(
    private val repository: ChatRepository
) {

    operator fun invoke(): Flow<List<UserChat>>{
        return repository.getUserChats()
    }
}