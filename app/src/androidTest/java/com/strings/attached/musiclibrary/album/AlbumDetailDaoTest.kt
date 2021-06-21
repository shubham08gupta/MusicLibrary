package com.strings.attached.musiclibrary.album

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.strings.attached.musiclibrary.db.AlbumDao
import com.strings.attached.musiclibrary.db.AppDatabase
import com.strings.attached.musiclibrary.db.TrackDao
import com.strings.attached.musiclibrary.util.TestUtil
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AlbumDetailDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var trackDao: TrackDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()
        albumDao = db.albumDao()
        trackDao = db.trackDao()
    }

    @Test
    fun writeAlbumListAndReadInList() = runBlocking {
        val albums = TestUtil.createTestAlbums(5)
        albumDao.insert(albums)
        albumDao.getAllAlbums().test {
            assertEquals(albums, expectItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun writeAlbumAndRead() = runBlocking {
        val album = TestUtil.createTestAlbums(1).first()
        albumDao.insert(album)
        val dbAlbum = albumDao.getAlbumDetail(
            albumName = album.albumName,
            artistName = album.artistName
        )
        assertEquals(album, dbAlbum)
    }

    @Test
    fun writeAlbumWithTracksAndRead() = runBlocking {
        val albumWithTracks = TestUtil.createTestAlbumsWithTracks(1).first()
        albumDao.insert(albumWithTracks.album)
        trackDao.insert(albumWithTracks.tracks)
        val dbAlbumWithTracks = albumDao.getAlbumWithTracksDetail(
            albumName = albumWithTracks.album.albumName,
            artistName = albumWithTracks.album.artistName
        )
        assertEquals(albumWithTracks, dbAlbumWithTracks)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}