package com.example.suruchat_app.domain.use_cases

import com.example.suruchat_app.domain.models.User
import com.example.suruchat_app.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetUsers(
    private val repository: ChatRepository
) {

    operator fun invoke(): Flow<List<User>>{
        return repository.getUsers()
    }
}