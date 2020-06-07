package ua.alxmute.migratemusic.strategy

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.DeezerSearchResponse
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.data.ServiceTrack

class DeezerMusicServiceStrategy(
    private val contextHolder: ContextHolder,
    private val httpClient: OkHttpClient,
    private val gson: Gson
) : MusicServiceStrategy {

    override fun requestTrack(searchQuery: String): ServiceTrack {
        val trackName = searchQuery.replace(" ", "+")

        val searchTrackRequest = Request.Builder()
            .url("https://api.deezer.com/search?q=$trackName&limit=1&access_token=${contextHolder.token}")
            .get()
            .build()

        val response = httpClient.newCall(searchTrackRequest).execute()

        if (response.isSuccessful) {
            val tracks = gson.fromJson(response.body()!!.string(), DeezerSearchResponse::class.java)

            if (tracks.total > 0) {
                val trackData = tracks.data[0]
                return ServiceTrack(tracks.total, trackData.id, trackData.title, trackData.artist.name)
            }
            return ServiceTrack(tracks.total)
        }
        throw RuntimeException("Cannot find track") // TODO: do not throw exception...
    }

    override fun addTrack(trackId: String): Boolean {
        val addTrackRequest = Request.Builder()
            .url("https://api.deezer.com/user/me/tracks?track_id=${trackId}&access_token=${contextHolder.token}")
            .post(RequestBody.create(null, ""))
            .build()

        val response = httpClient.newCall(addTrackRequest).execute()

        return response.isSuccessful
    }

    override fun getMusicServiceName(): MusicServiceName {
        return MusicServiceName.DEEZER
    }

}