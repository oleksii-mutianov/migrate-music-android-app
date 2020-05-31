package ua.alxmute.migratemusic.strategy

import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.data.SearchResponse

interface MusicServiceStrategy {

    fun requestTrack(searchQuery: String): SearchResponse.TracksWrapper

    fun addTrack(trackId: String): Boolean

    fun getMusicServiceName(): MusicServiceName

}