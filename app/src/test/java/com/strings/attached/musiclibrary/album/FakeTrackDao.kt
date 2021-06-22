package com.strings.attached.musiclibrary.album

import com.strings.attached.musiclibrary.db.TrackDao
import com.strings.attached.musiclibrary.model.album.Track

class FakeTrackDao(
    private val trackList: MutableList<Track> = mutableListOf()
) : TrackDao {
    override suspend fun insert(obj: Track) {
        trackList.add(obj)
    }

    override suspend fun insert(obj: List<Track>) {
        trackList.addAll(obj)
    }

    override suspend fun insertOrIgnore(obj: Track): Long {
        val sameObj = trackList.find {
            it.trackId == obj.trackId
        }
        return if (sameObj == null) {
            trackList.add(obj)
            (trackList.size - 1).toLong()
        } else {
            -1L
        }
    }

    override suspend fun update(obj: Track) {
        val target = trackList.find {
            it.trackId == obj.trackId
        }
        target?.let {
            trackList.remove(it)
            trackList.add(obj)
        }
    }

    override suspend fun delete(obj: Track) {
        trackList.remove(obj)
    }
}