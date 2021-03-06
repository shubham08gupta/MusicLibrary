package com.strings.attached.musiclibrary.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.strings.attached.musiclibrary.BuildConfig
import com.strings.attached.musiclibrary.api.QueryParameterInterceptor
import com.strings.attached.musiclibrary.api.album.AlbumApiService
import com.strings.attached.musiclibrary.api.artist.ArtistApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        queryParameterInterceptor: QueryParameterInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(queryParameterInterceptor)
        }.build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    fun provideQueryParameterInterceptor(): QueryParameterInterceptor = QueryParameterInterceptor()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().apply {
            baseUrl(BuildConfig.BASE_URL)
            client(okHttpClient)
            addConverterFactory(
                Json {
                    encodeDefaults = true
                    ignoreUnknownKeys = true
                }.asConverterFactory("application/json".toMediaType())
            )
        }.build()


    @Singleton
    @Provides
    fun provideArtistApiService(retrofit: Retrofit): ArtistApiService =
        retrofit.create(ArtistApiService::class.java)

    @Singleton
    @Provides
    fun provideAlbumApiService(retrofit: Retrofit): AlbumApiService =
        retrofit.create(AlbumApiService::class.java)
}
