package com.example.suruchat_app.data.remote

object HttpRoutes {

    const val BASE_URL ="https://suruchat-api.herokuapp.com"

    const val LOGIN = "$BASE_URL/auth/login"

    const val SIGNUP = "$BASE_URL/auth/signup"

    const val USER_CHATS = "$BASE_URL/user/userchats"

    const val APP_USERS = "$BASE_URL/user/appusers"

    const val START_CHAT = "$BASE_URL/user/startchat"

    const val GET_MESSAGES = "$BASE_URL/user/chatmessages"

    const val SEND_MESSAGE = "$BASE_URL/user/message"

    const val UPLOAD_IMAGE = "$BASE_URL/user/image"
}