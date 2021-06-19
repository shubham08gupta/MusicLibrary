package com.strings.attached.musiclibrary.model.artist

import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    val name: String,
    val mbid: String? = null,
    val url: String,
    val image: List<Image> = emptyList()
)