package ua.alxmute.migratemusic.service

import ua.alxmute.migratemusic.chain.AddTrackChain
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.LocalTrackDto
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.strategy.MusicServiceStrategy

class AddTracksService(
    private val musicServiceStrategies: Map<MusicServiceName, MusicServiceStrategy>,
    private val contextHolder: ContextHolder,
    private val addTrackChain: AddTrackChain
) {

    fun addTrack(localTrackDto: LocalTrackDto): Boolean {
        val musicServiceStrategy = musicServiceStrategies.getValue(contextHolder.musicServiceName)
        return addTrackChain.handle(localTrackDto, musicServiceStrategy)
    }

}