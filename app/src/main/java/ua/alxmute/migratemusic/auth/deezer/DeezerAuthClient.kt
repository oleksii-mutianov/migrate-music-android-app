package ua.alxmute.migratemusic.auth.deezer

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import ua.alxmute.migratemusic.activity.presenter.ChooseMusicServicePresenterImpl.Companion.DEEZER_CLIENT_ID
import ua.alxmute.migratemusic.auth.AuthClient
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.service.LoginListener
import kotlin.concurrent.thread

class DeezerAuthClient(
    private val httpClient: OkHttpClient,
    private val gson: Gson
) : AuthClient {

    override fun onComplete(code: String, loginListener: LoginListener) {
        thread {
            val getAccessTokenRequest = Request.Builder()
                .url("https://connect.deezer.com/oauth/access_token.php?app_id=$DEEZER_CLIENT_ID&secret=2a84c287fd8373a3d1e18af78548fd5c&code=$code&output=json")
                .get()
                .build()

            val response = httpClient.newCall(getAccessTokenRequest).execute()
            if (response.isSuccessful) {
                val deezerResponse = gson.fromJson(response.body()!!.string(), DeezerResponse::class.java)
                loginListener.onLoggedIn(deezerResponse.accessToken, MusicServiceName.DEEZER)
            }
        }
    }
}