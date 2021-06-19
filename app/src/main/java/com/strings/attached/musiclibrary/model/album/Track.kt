package com.strings.attached.musiclibrary.model.album

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Track(
    @SerialName("name") var name: String,
    @SerialName("url") var url: String,
    @SerialName("duration") var duration: String
)