package com.example.suruchat_app.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(
    val imageurl: String
)

@Serializable
data class SendImageResponse(
    val image: String
)
