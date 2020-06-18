package ua.alxmute.migratemusic.strategy

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.data.ServiceTrack
import ua.alxmute.migratemusic.data.response.SpotifySearchResponse

class SpotifyMusicServiceStrategy(
    private val contextHolder: ContextHolder,
    private val httpClient: OkHttpClient,
    private val gson: Gson
) : MusicServiceStrategy {

    override fun requestTrack(searchQuery: String): ServiceTrack {

        val trackName = searchQuery.replace(" ", "+")

        val searchTrackRequest = Request.Builder()
            .url("https://api.spotify.com/v1/search?type=track&q=$trackName&limit=1")
            .addHeader("Authorization", "Bearer ${contextHolder.token}")
            .get()
            .build()

        val response = httpClient.newCall(searchTrackRequest).execute()

        if (response.isSuccessful) {
            val tracks = gson.fromJson(response.body()!!.string(), SpotifySearchResponse::class.java).tracks
            if (tracks.total > 0) {
                val trackResponse = tracks.items[0]
                return ServiceTrack(tracks.total, trackResponse.id, trackResponse.name, trackResponse.artists[0].name)
            }
        }
        return ServiceTrack(0)
    }

    override fun addTrack(trackId: String): Boolean {
        val addTrackRequest = Request.Builder()
            .url("https://api.spotify.com/v1/me/tracks?ids=$trackId")
            .addHeader("Authorization", "Bearer ${contextHolder.token}")
            .put(RequestBody.create(null, ""))
            .build()

        return httpClient.newCall(addTrackRequest).execute().isSuccessful
    }

    override fun getMusicServiceName(): MusicServiceName {
        return MusicServiceName.SPOTIFY
    }

}