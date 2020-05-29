package ua.alxmute.migratemusic.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.SearchResponse
import ua.alxmute.migratemusic.data.SearchResponse.TracksWrapper.TrackResponse
import javax.inject.Inject

class SpotifyService
@Inject constructor(
    val contextHolder: ContextHolder,
    val httpClient: OkHttpClient,
    val gson: Gson
) {

    fun requestTrack(track: String): TrackResponse {

        val trackName = track.replace(" ", "+")

        val searchTrackRequest = Request.Builder()
            .url("https://api.spotify.com/v1/search?type=track&q=$trackName&limit=1")
            .addHeader("Authorization", "Bearer ${contextHolder.token}")
            .get()
            .build()

        val response = httpClient.newCall(searchTrackRequest).execute()

        if (response.isSuccessful) {
            val result = gson.fromJson(response.body()!!.string(), SearchResponse::class.java).tracks
            if (result.total > 0) {
                return result.items[0]
            }
        }
        throw RuntimeException("Cannot find track") // TODO: do not throw exception...
    }

    fun addTrack(trackId: String): Boolean {
        val addTrackRequest = Request.Builder()
            .url("https://api.spotify.com/v1/me/tracks?ids=$trackId")
            .addHeader("Authorization", "Bearer ${contextHolder.token}")
            .get()
            .build()

        val response = httpClient.newCall(addTrackRequest).execute()

        return response.isSuccessful
    }


}