package com.strings.attached.musiclibrary.model.album

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = AlbumDetail::class,
            parentColumns = ["albumId"],
            childColumns = ["trackAlbumId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index(value = ["trackAlbumId"])]
)
@Serializable
data class Track(
    @PrimaryKey(autoGenerate = true) val trackId: Long = 0,
    @Transient var trackAlbumId: Long = -1, // acts as a Foreign Key
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
    @SerialName("duration") val duration: String
)