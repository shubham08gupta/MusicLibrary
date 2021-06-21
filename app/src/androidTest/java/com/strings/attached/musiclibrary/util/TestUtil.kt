package com.strings.attached.musiclibrary.util

import com.strings.attached.musiclibrary.model.album.AlbumDetail
import com.strings.attached.musiclibrary.model.album.AlbumWithTracks
import com.strings.attached.musiclibrary.model.album.Track

object TestUtil {

    fun createTestAlbums(howMany: Int): List<AlbumDetail> {
        val list = listOf(
            AlbumDetail(1, "album1", "artist1", "url1", emptyList(), "albumUrl1", "123", 12, null),
            AlbumDetail(2, "album2", "artist2", "url2", emptyList(), "albumUrl2", "12", 121, null),
            AlbumDetail(3, "album3", "artist3", "url3", emptyList(), "albumUrl3", "3", 124, null),
            AlbumDetail(4, "album4", "artist4", "url4", emptyList(), "albumUrl4", "23", 112, null),
            AlbumDetail(5, "album5", "artist5", "url5", emptyList(), "albumUrl5", "13", 142, null),
        )
        return list.take(howMany)
    }

    fun createTestAlbumsWithTracks(howMany: Int): List<AlbumWithTracks> {
        val list = listOf(
            AlbumWithTracks(
                AlbumDetail(
                    1,
                    "album1",
                    "artist1",
                    "url1",
                    emptyList(),
                    "albumUrl1",
                    "123",
                    12,
                    null
                ), tracks = listOf(
                    Track(1, 1, "track1", "trackUrl1", "234")
                )
            ),
            AlbumWithTracks(
                AlbumDetail(
                    2,
                    "album2",
                    "artist2",
                    "url2",
                    emptyList(),
                    "albumUrl2",
                    "1231",
                    124,
                    null
                ), tracks = listOf(
                    Track(2, 2, "track2", "trackUrl2", "24")
                )
            ),
            AlbumWithTracks(
                AlbumDetail(
                    3,
                    "album3",
                    "artist3",
                    "url3",
                    emptyList(),
                    "albumUrl3",
                    "1213",
                    512,
                    null
                ), tracks = listOf(
                    Track(3, 3, "track3", "trackUrl3", "345")
                )
            ),
        )
        return list.take(howMany)
    }
}