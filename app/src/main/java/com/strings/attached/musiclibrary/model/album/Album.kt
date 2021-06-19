package com.strings.attached.musiclibrary.model.album

import com.strings.attached.musiclibrary.model.artist.Artist
import com.strings.attached.musiclibrary.model.artist.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val name: String,
    @SerialName("playcount") val playCount: Int,
    val mbid: String,
    val url: String,
    val artist: Artist,
    val image: List<Image>
)
