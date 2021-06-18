package com.strings.attached.musiclibrary.model.artist

import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    val name: String,
    val listeners: String,
    val mbid: String,
    val url: String,
    val streamable: String,
    val image: List<Image>
)