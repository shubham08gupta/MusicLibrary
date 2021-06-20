package com.strings.attached.musiclibrary.db

import androidx.room.Dao
import com.strings.attached.musiclibrary.model.album.Track

@Dao
interface TrackDao : BaseDao<Track>
