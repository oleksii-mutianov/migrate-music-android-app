package ua.alxmute.migratemusic.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import ua.alxmute.migratemusic.chain.AddTrackChain
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.di.MusicServiceNameKey
import ua.alxmute.migratemusic.service.MusicProcessorService
import ua.alxmute.migratemusic.strategy.DeezerMusicServiceStrategy
import ua.alxmute.migratemusic.strategy.MusicServiceStrategy
import ua.alxmute.migratemusic.strategy.SpotifyMusicServiceStrategy
import javax.inject.Singleton

@Module
class MusicServiceStrategyModule {

    @IntoMap
    @Provides
    @Singleton
    @MusicServiceNameKey(MusicServiceName.SPOTIFY)
    fun spotifyMusicServiceStrategy(
        contextHolder: ContextHolder,
        httpClient: OkHttpClient,
        gson: Gson
    ): MusicServiceStrategy = SpotifyMusicServiceStrategy(contextHolder, httpClient, gson)

    // TODO: just mock implementation, need to create real one
    @IntoMap
    @Provides
    @Singleton
    @MusicServiceNameKey(MusicServiceName.DEEZER)
    fun deezerMusicServiceStrategy(

    ): MusicServiceStrategy = DeezerMusicServiceStrategy()

    @Provides
    @Singleton
    @JvmSuppressWildcards
    fun musicProcessorService(
        strategies: Map<MusicServiceName, MusicServiceStrategy>,
        contextHolder: ContextHolder,
        addTrackChain: AddTrackChain
    ): MusicProcessorService = MusicProcessorService(strategies, contextHolder, addTrackChain)
}