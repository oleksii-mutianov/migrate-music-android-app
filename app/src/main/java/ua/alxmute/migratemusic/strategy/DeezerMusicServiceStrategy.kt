package ua.alxmute.migratemusic.strategy

import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.data.SearchResponse

class DeezerMusicServiceStrategy : MusicServiceStrategy {
    override fun requestTrack(searchQuery: String): SearchResponse.TracksWrapper {
        TODO("Not yet implemented")
    }

    override fun addTrack(trackId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getMusicServiceName(): MusicServiceName {
        return MusicServiceName.DEEZER
    }
}