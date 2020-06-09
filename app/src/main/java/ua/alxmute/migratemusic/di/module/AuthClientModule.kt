package ua.alxmute.migratemusic.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import ua.alxmute.migratemusic.auth.AuthClient
import ua.alxmute.migratemusic.auth.deezer.DeezerAuthClient
import ua.alxmute.migratemusic.auth.youtube.YoutubeMusicAuthClient
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.di.MusicServiceNameKey
import javax.inject.Singleton

@Module
class AuthClientModule {

    @IntoMap
    @Provides
    @Singleton
    @MusicServiceNameKey(MusicServiceName.DEEZER)
    fun deezerAuthClient(
        httpClient: OkHttpClient,
        gson: Gson
    ): AuthClient = DeezerAuthClient(httpClient, gson)

    @IntoMap
    @Provides
    @Singleton
    @MusicServiceNameKey(MusicServiceName.YOUTUBE_MUSIC)
    fun youtubeMusicAuthClient(): AuthClient = YoutubeMusicAuthClient()

}