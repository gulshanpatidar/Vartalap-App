package com.example.suruchat_app.domain.use_cases

import com.example.suruchat_app.domain.models.InvalidUserException
import com.example.suruchat_app.domain.models.User
import com.example.suruchat_app.domain.repository.ChatRepository
import kotlin.jvm.Throws

class AddUser(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(user: User){
        repository.insertUser(user)
    }
}