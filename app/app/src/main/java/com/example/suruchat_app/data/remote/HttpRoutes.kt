package com.example.suruchat_app.data.remote

object HttpRoutes {

    const val BASE_URL ="https://vartalap-api.herokuapp.com"

    const val LOGIN = "$BASE_URL/login"

    const val SIGNUP = "$BASE_URL/signup"

    const val USER_CHATS = "$BASE_URL/userchats"

    const val APP_USERS = "$BASE_URL/appusers/"

    const val START_CHAT = "$BASE_URL/startchat/"

    const val GET_MESSAGES = "$BASE_URL/chatmessages"

    const val SEND_MESSAGE = "$BASE_URL/message/"

    const val UPLOAD_IMAGE = "$BASE_URL/image/"

    const val SEND_IMAGE = "$BASE_URL/sendimage/"

    const val UPDATE_PROFILE = "$BASE_URL/updatefullname/"
}