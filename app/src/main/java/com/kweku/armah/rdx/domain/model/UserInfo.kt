package com.kweku.armah.rdx.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerialName("firstName") val firstName: String = "",
    @SerialName("lastName") val lastName: String = "",
    @SerialName("email") val email: String = "",
    @SerialName("phoneNumber") val phoneNumber: String = "",
    @SerialName("pin") val pin: String = ""
)