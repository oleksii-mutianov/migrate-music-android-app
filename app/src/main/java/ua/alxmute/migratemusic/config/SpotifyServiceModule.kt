package ua.alxmute.migratemusic.config

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.service.SpotifyService
import javax.inject.Singleton

@Module
class SpotifyServiceModule {

    @Provides
    @Singleton
    fun spotifyService(
        contextHolder: ContextHolder,
        httpClient: OkHttpClient,
        gson: Gson
    ): SpotifyService = SpotifyService(contextHolder, httpClient, gson)

}