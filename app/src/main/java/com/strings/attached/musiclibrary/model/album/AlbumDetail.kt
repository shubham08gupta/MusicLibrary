package com.strings.attached.musiclibrary.model.album

import com.strings.attached.musiclibrary.model.artist.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/***
 * The details of an [Album] has a different response format. Therefore, we need to create a
 * different class for details.
 */
@Serializable
data class AlbumDetail(
    val name: String,
    @SerialName("artist") val artistName: String,
    val url: String,
    val image: List<Image> = emptyList(),
    val listeners: String,
    @SerialName("playcount") val playCount: Int,
    val tracks: Tracks? = null
)

@Serializable
data class Tracks(
    @SerialName("track") val tracks: List<Track> = emptyList()
)
