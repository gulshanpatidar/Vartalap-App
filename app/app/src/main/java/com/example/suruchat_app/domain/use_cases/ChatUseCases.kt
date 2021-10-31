package com.example.suruchat_app.domain.use_cases

data class ChatUseCases(
    val addUser: AddUser,
    val getUsers: GetUsers,
    val addUserChat: AddUserChat,
    val getUserChats: GetUserChats,
    val addMessage: AddMessage,
    val getMessages: GetMessages
)