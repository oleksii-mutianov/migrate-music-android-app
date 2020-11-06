package ua.alxmute.migratemusic.service.chain

import ua.alxmute.migratemusic.data.LocalTrackDto
import ua.alxmute.migratemusic.service.strategy.MusicServiceStrategy

abstract class AddTrackChain {

    private var next: AddTrackChain? = null

    protected abstract fun getQueryString(localTrackDto: LocalTrackDto): String

    fun setNext(addTrackChain: AddTrackChain): AddTrackChain {
        next = addTrackChain
        return addTrackChain
    }

    fun handle(localTrackDto: LocalTrackDto, musicServiceStrategy: MusicServiceStrategy): Boolean {
        val searchQuery = getQueryString(localTrackDto).replace(" ", "+")

        if (searchQuery.isNotBlank()) {
            musicServiceStrategy.requestTrack(searchQuery)?.let { track ->
                return musicServiceStrategy.addTrack(track.id)
            }
        }

        return handleNext(localTrackDto, musicServiceStrategy)
    }

    private fun handleNext(localTrackDto: LocalTrackDto, musicServiceStrategy: MusicServiceStrategy) =
        next?.handle(localTrackDto, musicServiceStrategy) ?: false

}