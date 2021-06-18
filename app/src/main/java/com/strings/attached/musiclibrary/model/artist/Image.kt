package com.strings.attached.musiclibrary.model.artist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    @SerialName("#text") val text: String,
    val size: String
)