package com.strings.attached.musiclibrary.api.album

import com.strings.attached.musiclibrary.model.album.Album
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopAlbumsResponse(
    @SerialName("topalbums") val topAlbums: List<Album>? = null,
    val error: String? = null,
    val message: String? = null,
)
