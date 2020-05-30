package ua.alxmute.migratemusic.service

import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.LocalTrackDto
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.strategy.MusicServiceStrategy

class AddTracksService(
    private val musicServiceStrategies: Map<MusicServiceName, MusicServiceStrategy>,
    private val contextHolder: ContextHolder
) {

    fun addTrack(localTrackDto: LocalTrackDto): Boolean {

        val musicServiceStrategy = musicServiceStrategies[contextHolder.musicServiceName]
        val foundTracks = musicServiceStrategy?.requestTrack("${localTrackDto.author} ${localTrackDto.title}")

        // TODO: there must be chain of responsibility to find track id

        return if (foundTracks?.total!! > 0) {
            val id = foundTracks.items[0].id
            return musicServiceStrategy.addTrack(id)
        } else {
            false
        }

    }

}