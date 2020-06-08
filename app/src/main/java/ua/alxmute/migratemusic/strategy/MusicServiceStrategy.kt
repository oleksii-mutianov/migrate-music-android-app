package ua.alxmute.migratemusic.strategy

import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.data.ServiceTrack

interface MusicServiceStrategy {

    fun requestTrack(searchQuery: String): ServiceTrack

    fun addTrack(trackId: String): Boolean

    fun getMusicServiceName(): MusicServiceName

}