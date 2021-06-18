package com.strings.attached.musiclibrary.api.artist

import retrofit2.http.GET
import retrofit2.http.Query

interface ArtistApiService {

    @GET("?method=artist.search")
    suspend fun searchArtistsByName(
        @Query("artist") name: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): ArtistSearchResponse
}