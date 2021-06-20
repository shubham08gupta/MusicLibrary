package com.strings.attached.musiclibrary.di

import android.content.Context
import androidx.room.Room
import com.strings.attached.musiclibrary.db.AlbumDao
import com.strings.attached.musiclibrary.db.AppDatabase
import com.strings.attached.musiclibrary.db.TrackDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/***
 * A Dagger Module to provide configurations specific to the Application database(a.k.a. [Room])
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "AppDatabase.db"
        ).build()

    @Singleton
    @Provides
    fun provideAlbumDao(db: AppDatabase): AlbumDao = db.albumDao()

    @Singleton
    @Provides
    fun provideTrackDao(db: AppDatabase): TrackDao = db.trackDao()

}
