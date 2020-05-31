package ua.alxmute.migratemusic.chain

import ua.alxmute.migratemusic.data.LocalTrackDto
import ua.alxmute.migratemusic.strategy.MusicServiceStrategy

abstract class AddTrackChain {

    private var next: AddTrackChain? = null

    protected abstract fun getQueryString(localTrackDto: LocalTrackDto): String

    fun setNext(addTrackChain: AddTrackChain): AddTrackChain {
        next = addTrackChain
        return addTrackChain
    }

    fun handle(localTrackDto: LocalTrackDto, musicServiceStrategy: MusicServiceStrategy): Boolean {
        val searchQuery = getQueryString(localTrackDto)

        if (searchQuery.isNotBlank()) {
            val foundTracks = musicServiceStrategy.requestTrack(searchQuery)
            if ((foundTracks.total > 0)) {
                return musicServiceStrategy.addTrack(foundTracks.items[0].id)
            }
        }

        return handleNext(localTrackDto, musicServiceStrategy)
    }

    private fun handleNext(localTrackDto: LocalTrackDto, musicServiceStrategy: MusicServiceStrategy): Boolean {
        return if ((next != null)) {
            next!!.handle(localTrackDto, musicServiceStrategy)
        } else {
            false
        }
    }

}