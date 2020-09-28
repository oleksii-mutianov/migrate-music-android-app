package ua.alxmute.migratemusic.service.factory

import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.service.strategy.DeezerMusicServiceStrategy
import ua.alxmute.migratemusic.service.strategy.MusicServiceStrategy
import ua.alxmute.migratemusic.service.strategy.SpotifyMusicServiceStrategy

object MusicServiceStrategyFactory {

    private val musicServiceStrategies: Map<MusicServiceName, MusicServiceStrategy> = mapOf(
        MusicServiceName.DEEZER to DeezerMusicServiceStrategy,
        MusicServiceName.SPOTIFY to SpotifyMusicServiceStrategy,
    )

    operator fun get(musicServiceName: MusicServiceName) =
        musicServiceStrategies.getValue(musicServiceName)

}