package ua.alxmute.migratemusic.strategy

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.data.ServiceTrack
import ua.alxmute.migratemusic.data.response.YoutubeMusicSearchResponse

class YoutubeMusicServiceStrategy(
    private val contextHolder: ContextHolder,
    private val httpClient: OkHttpClient,
    private val gson: Gson
) : MusicServiceStrategy {

    override fun requestTrack(searchQuery: String): ServiceTrack {
        val trackName = searchQuery.replace(" ", "+")

        val searchTrackRequest = Request.Builder()
            .url(
                "https://www.googleapis.com/youtube/v3/search?q=$trackName&access_token=${contextHolder.token}"
                        + "&part=snippet&type=video&maxResults=1&videoCategoryId=10"
            )
            .get()
            .build()

        val response = httpClient.newCall(searchTrackRequest).execute()

        if (response.isSuccessful) {
            val tracks = gson.fromJson(response.body()!!.string(), YoutubeMusicSearchResponse::class.java)
            if (tracks.pageInfo.totalResults > 0) {
                val trackResponse = tracks.items[0]
                return ServiceTrack(tracks.pageInfo.totalResults, trackResponse.id.videoId, trackResponse.snippet.title, "")
            }
        }
        return ServiceTrack(0)
    }

    override fun addTrack(trackId: String): Boolean {
        val addTrackRequest = Request.Builder()
            .url("https://www.googleapis.com/youtube/v3/videos/rate?id=${trackId}&rating=like&access_token=${contextHolder.token}")
            .post(RequestBody.create(null, ""))
            .build()

        return httpClient.newCall(addTrackRequest).execute().isSuccessful
    }

    override fun getMusicServiceName(): MusicServiceName = MusicServiceName.YOUTUBE_MUSIC
}