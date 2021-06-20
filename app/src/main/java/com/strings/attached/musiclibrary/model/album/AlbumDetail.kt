package com.strings.attached.musiclibrary.model.album

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.strings.attached.musiclibrary.model.artist.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/***
 * The details of an [Album] has a different response format. Therefore, we need to create a
 * different class for details. Creates its own primary key as the server does not returns
 * a unique id.
 */
@Entity
@Serializable
data class AlbumDetail(
    @PrimaryKey(autoGenerate = true) val albumId: Long = 0,
    @SerialName("name") val albumName: String,
    @SerialName("artist") val artistName: String,
    @SerialName("url") val url: String,
    @SerialName("image") @Ignore val image: List<Image> = emptyList(),
    @Transient val albumImageUrl: String? = null, // choose an image from List<Image> to save in DB
    @SerialName("listeners") val listeners: String,
    @SerialName("playcount") val playCount: Int,
    @SerialName("tracks") @Ignore val tracks: Tracks? = null
) {
    constructor(
        albumId: Long,
        albumName: String,
        artistName: String,
        url: String,
        albumImageUrl: String?,
        listeners: String,
        playCount: Int
    ) : this(albumId, albumName, artistName, url, emptyList(), albumImageUrl, listeners, playCount)
}

@Serializable
data class Tracks(
    @SerialName("track") val tracks: List<Track> = emptyList()
)
