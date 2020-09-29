package ua.alxmute.migratemusic.service.strategy

import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.data.ServiceTrack
import ua.alxmute.migratemusic.data.response.SpotifySearchResponse
import ua.alxmute.migratemusic.service.HttpClient
import ua.alxmute.migratemusic.service.HttpClient.json
import ua.alxmute.migratemusic.service.JSON

object SpotifyMusicServiceStrategy : MusicServiceStrategy {

    override fun requestTrack(searchQuery: String): ServiceTrack? {

        val response = HttpClient.get(
            "https://api.spotify.com/v1/search?type=track&q=$searchQuery&limit=1",
            "Authorization" to "Bearer ${ContextHolder.token}"
        )

        if (response.isSuccessful) {
            val searchResponse = JSON.fromJson(response.json(), SpotifySearchResponse::class)
            searchResponse.tracks.items.firstOrNull()?.let {
                return ServiceTrack(it.id, it.name, it.artists[0].name)
            }
        }

        return null
    }

    override fun addTrack(trackId: String): Boolean {
        val response = HttpClient.put(
            "https://api.spotify.com/v1/me/tracks?ids=$trackId",
            "Authorization" to "Bearer ${ContextHolder.token}"
        )
        return response.isSuccessful
    }

    override fun getMusicServiceName() = MusicServiceName.SPOTIFY

}