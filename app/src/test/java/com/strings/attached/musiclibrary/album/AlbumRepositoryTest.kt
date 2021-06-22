package com.strings.attached.musiclibrary.album

import app.cash.turbine.test
import com.strings.attached.musiclibrary.data.album.AlbumRepository
import com.strings.attached.musiclibrary.data.album.AlbumRepositoryImpl
import com.strings.attached.musiclibrary.model.album.AlbumDetail
import com.strings.attached.musiclibrary.model.album.AlbumWithTracks
import com.strings.attached.musiclibrary.model.album.Track
import com.strings.attached.musiclibrary.model.album.Tracks
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import java.io.IOException

class AlbumRepositoryTest {

    private lateinit var albumRepository: AlbumRepository
    private lateinit var fakeAlbumDao: FakeAlbumDao
    private lateinit var fakeTrackDao: FakeTrackDao
    private lateinit var fakeApiService: FakeAlbumService

    //region prepare test data
    private val trackList = mutableListOf(
        Track(1, 1, "track1", "trackUrl1", "234"),
        Track(2, 1, "track2", "trackUrl2", "34"),
        Track(3, 2, "track3", "trackUrl3", "414"),
        Track(4, 3, "track4", "trackUrl3", "300"),
    )
    private val albumList = mutableListOf(
        AlbumDetail(
            1, "album1", "artist1", "url1", emptyList(), "albumUrl1", "123", 12,
            Tracks(trackList.take(2))
        ),
        AlbumDetail(
            2, "album2", "artist2", "url2", emptyList(), "albumUrl2", "12", 121,
            Tracks(listOf(trackList[2]))
        ),
        AlbumDetail(
            3, "album3", "artist3", "url3", emptyList(), "albumUrl3", "3", 124,
            Tracks(listOf(trackList[3]))
        ),
        AlbumDetail(4, "album4", "artist4", "url4", emptyList(), "albumUrl4", "23", 112, null),
        AlbumDetail(5, "album5", "artist5", "url5", emptyList(), "albumUrl5", "13", 142, null),
    )
    //endregion

    @Before
    fun setup() {
        fakeAlbumDao = FakeAlbumDao(albumList)
        fakeTrackDao = FakeTrackDao(trackList)
        fakeApiService = FakeAlbumService()
        albumRepository = AlbumRepositoryImpl(fakeApiService, fakeAlbumDao, fakeTrackDao)
    }

    @Test
    fun whenAlbumIsAvailableInDbThenReturnTrue() = runBlockingTest {
        val isAvailable = albumRepository.isAlbumAvailableLocally("album1", "artist1")
        assertThat(isAvailable, `is`(true))
    }

    @Test
    fun whenAlbumIsUnavailableInDbThenReturnFalse() = runBlockingTest {
        val isAvailable = albumRepository.isAlbumAvailableLocally("album123", "artist123")
        assertThat(isAvailable, `is`(false))
    }

    @Test
    fun whenSaveAlbumCalledThenSaveAlbumInDb() = runBlockingTest {
        // save a new album in the DB with its tracks
        val trackList = listOf(
            Track(123, 321, "track1", "trackUrl1", "234"),
            Track(124, 321, "track2", "trackUrl2", "34"),
        )
        val albumWithTracksToStore = AlbumWithTracks(
            album = AlbumDetail(
                321,
                "albumTest",
                "artistTest",
                "url1",
                emptyList(),
                "albumUrl1",
                "123",
                12,
                Tracks(trackList)
            ),
            tracks = trackList
        )
        albumRepository.saveAlbumWithTracksLocally(albumWithTracksToStore)
        // find the saved album in the DB and verify
        val storedAlbumWithTracks =
            fakeAlbumDao.getAlbumWithTracksDetail(
                albumName = "albumTest",
                artistName = "artistTest"
            )
        assertThat(storedAlbumWithTracks, `is`(equalTo(albumWithTracksToStore)))
    }

    @Test
    fun whenDeleteAlbumCalledThenDeleteAlbumFromDb() = runBlockingTest {
        val albumToDelete = AlbumWithTracks(albumList.first(), trackList.take(2))
        albumRepository.deleteLocalAlbum(albumToDelete)
        val fetchDeletedAlbum = fakeAlbumDao.getAlbumWithTracksDetail(
            albumName = albumToDelete.album.albumName,
            artistName = albumToDelete.album.artistName
        )
        // ensure that the album is deleted by fetching the deleted album which should return null
        assertThat(fetchDeletedAlbum, nullValue())
    }

    @Test
    fun whenFetchSavedAlbumsThenReturnAllLocalAlbums() = runBlockingTest {
        albumList.clear() // clear any saved items to keep the example small
        val albumsToInsert = listOf(
            AlbumDetail(1, "album1", "artist1", "url1", emptyList(), "albumUrl1", "123", 12, null),
            AlbumDetail(2, "album2", "artist2", "url2", emptyList(), "albumUrl2", "12", 121, null),
        )
        fakeAlbumDao.insert(albumsToInsert)
        // test Kotlin Flow emission
        albumRepository.getLocallySavedAlbums().test {
            assertThat(expectItem().size, `is`(2))
            expectComplete()
        }
    }

    @Test
    fun whenFetchAlbumDetailsThenReturnDbAlbum() = runBlockingTest {
        // simulate internet not available environment by throwing an exception
        fakeApiService.exception = IOException("Internet is unavailable")
        val dbAlbumResult = albumRepository.getAlbumDetail(
            artistName = albumList[0].artistName,
            albumName = albumList[0].albumName
        )
        assertThat(dbAlbumResult.isSuccess, `is`(true))
    }

    @Test
    fun whenFetchAlbumDetailsThenReturnNetworkAlbum() = runBlockingTest {
        // add network data
        fakeApiService.albumDetail =
            AlbumDetail(
                481,
                "IceDaddy",
                "Gucci Mane",
                "url1",
                emptyList(),
                "albumUrl1",
                "123",
                12,
                Tracks(
                    listOf(
                        Track(678, 481, "Poppin", "PoppinUrl", "334"),
                        Track(679, 481, "I Got it", "I Got it Url", "211"),
                    )
                )
            )

        val networkAlbumResult = albumRepository.getAlbumDetail(
            artistName = albumList[0].artistName,
            albumName = albumList[0].albumName
        )
        assertThat(networkAlbumResult.isSuccess, `is`(true))
        networkAlbumResult.onSuccess {
            assertThat(it.album.albumId, `is`(481))
            assertThat(it.album.tracks?.tracks?.size, `is`(2))
        }
    }

    @Test
    fun whenFetchAlbumDetailsThenReturnFailure() = runBlockingTest {
        // simulate internet not available environment by throwing an exception
        fakeApiService.exception = IOException("Internet is unavailable")
        // clear DB data
        albumList.clear()
        val result = albumRepository.getAlbumDetail("artist1", "album1")
        assertThat(result.isFailure, `is`(true))
    }
}