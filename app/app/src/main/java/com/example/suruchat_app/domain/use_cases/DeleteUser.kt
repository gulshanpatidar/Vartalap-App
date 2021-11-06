package com.example.suruchat_app.domain.use_cases

import com.example.suruchat_app.domain.models.User
import com.example.suruchat_app.domain.repository.ChatRepository

class DeleteUser(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(user: User){
        repository.deleteUser(user)
    }
}