package com.strings.attached.musiclibrary.api.album

import com.strings.attached.musiclibrary.model.album.AlbumDetail
import kotlinx.serialization.Serializable

@Serializable
data class AlbumDetailResponse(
    val album: AlbumDetail? = null,
    val error: String? = null,
    val message: String? = null,
)
