package com.strings.attached.musiclibrary.api.album

import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumApiService {

    @GET("?method=artist.gettopalbums")
    suspend fun getTopAlbumsByArtistName(
        @Query("artist") name: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): TopAlbumsResponse
}