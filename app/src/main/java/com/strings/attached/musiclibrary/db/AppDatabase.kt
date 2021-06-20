package com.strings.attached.musiclibrary.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.strings.attached.musiclibrary.model.album.AlbumDetail
import com.strings.attached.musiclibrary.model.album.Track

@Database(
    entities = [
        AlbumDetail::class,
        Track::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao
    abstract fun trackDao(): TrackDao
}