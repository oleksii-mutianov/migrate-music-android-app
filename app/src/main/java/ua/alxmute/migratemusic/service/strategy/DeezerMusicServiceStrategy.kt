package ua.alxmute.migratemusic.service.strategy

import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.data.ServiceTrack
import ua.alxmute.migratemusic.data.response.DeezerSearchResponse
import ua.alxmute.migratemusic.service.HttpClient
import ua.alxmute.migratemusic.service.HttpClient.json
import ua.alxmute.migratemusic.service.JSON

object DeezerMusicServiceStrategy : MusicServiceStrategy {

    override fun requestTrack(searchQuery: String): ServiceTrack? {

        val response =
            HttpClient.get("https://api.deezer.com/search?q=$searchQuery&limit=1&access_token=${ContextHolder.token}")

        if (response.isSuccessful) {
            val searchResponse: DeezerSearchResponse = JSON.fromJson(response.json())
            searchResponse.data.firstOrNull()?.let {
                return ServiceTrack(it.id, it.title, it.artist.name)
            }
        }

        return null
    }

    override fun addTrack(trackId: String): Boolean {
        val response =
            HttpClient.post("https://api.deezer.com/user/me/tracks?track_id=${trackId}&access_token=${ContextHolder.token}")

        return response.isSuccessful
    }

    override fun getMusicServiceName() = MusicServiceName.DEEZER

}