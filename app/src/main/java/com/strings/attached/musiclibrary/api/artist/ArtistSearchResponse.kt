package com.strings.attached.musiclibrary.api.artist

import com.strings.attached.musiclibrary.model.artist.Artist
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistSearchResponse(
    val results: Results? = null,
    val error: String? = null,
    val message: String? = null,
)

@Serializable
data class Results(
    @SerialName("opensearch:totalResults") val totalResults: String,
    @SerialName("opensearch:startIndex") val startIndex: String,
    @SerialName("opensearch:itemsPerPage") val itemsPerPage: String,
    @SerialName("artistmatches") val artistMatches: ArtistMatches,
)

@Serializable
data class ArtistMatches(
    val artist: List<Artist>
)
