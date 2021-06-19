package com.strings.attached.musiclibrary.di

import com.strings.attached.musiclibrary.data.album.AlbumRepository
import com.strings.attached.musiclibrary.data.album.AlbumRepositoryImpl
import com.strings.attached.musiclibrary.data.artist.ArtistSearchRepository
import com.strings.attached.musiclibrary.data.artist.ArtistSearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/***
 * A Dagger Module used to provide configurations specific to Repository layer.
 * The repositories provided here are available application wide.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindArtistRepository(
        artistsRepositoryImpl: ArtistSearchRepositoryImpl
    ): ArtistSearchRepository

    @Binds
    abstract fun bindAlbumRepository(
        albumRepositoryImpl: AlbumRepositoryImpl
    ): AlbumRepository
}