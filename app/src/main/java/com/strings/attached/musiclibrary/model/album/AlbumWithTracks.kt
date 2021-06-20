package com.strings.attached.musiclibrary.model.album

import androidx.room.Embedded
import androidx.room.Relation

/***
 * A one-to-many relationship between [AlbumDetail] and [Track].
 */
data class AlbumWithTracks(
    @Embedded val album: AlbumDetail,
    @Relation(
        parentColumn = "albumId",
        entityColumn = "trackId"
    )
    val tracks: List<Track>
)