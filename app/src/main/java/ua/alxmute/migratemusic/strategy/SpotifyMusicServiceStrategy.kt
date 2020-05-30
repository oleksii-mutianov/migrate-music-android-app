package ua.alxmute.migratemusic.strategy

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.data.SearchResponse
import ua.alxmute.migratemusic.data.SearchResponse.TracksWrapper

class SpotifyMusicServiceStrategy(
    private val contextHolder: ContextHolder,
    private val httpClient: OkHttpClient,
    private val gson: Gson
) : MusicServiceStrategy {

    override fun requestTrack(searchQuery: String): TracksWrapper {

        val trackName = searchQuery.replace(" ", "+")

        val searchTrackRequest = Request.Builder()
            .url("https://api.spotify.com/v1/search?type=track&q=$trackName&limit=1")
            .addHeader("Authorization", "Bearer ${contextHolder.token}")
            .get()
            .build()

        val response = httpClient.newCall(searchTrackRequest).execute()

        if (response.isSuccessful) {
            return gson.fromJson(response.body()!!.string(), SearchResponse::class.java).tracks
        }
        throw RuntimeException("Cannot find track") // TODO: do not throw exception...
    }

    override fun addTrack(trackId: String): Boolean {
        val addTrackRequest = Request.Builder()
            .url("https://api.spotify.com/v1/me/tracks?ids=$trackId")
            .addHeader("Authorization", "Bearer ${contextHolder.token}")
            .put(RequestBody.create(null, ""))
            .build()

        val response = httpClient.newCall(addTrackRequest).execute()

        return response.isSuccessful
    }

    override fun getMusicServiceName(): MusicServiceName {
        return MusicServiceName.SPOTIFY
    }

}