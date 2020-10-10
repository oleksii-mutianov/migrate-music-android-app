package ua.alxmute.migratemusic.mvp.presenter

import android.content.Intent
import android.net.Uri
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.auth.AuthClient
import ua.alxmute.migratemusic.auth.data.AuthResponse
import ua.alxmute.migratemusic.auth.data.DeezerAuthRequest
import ua.alxmute.migratemusic.auth.data.YoutubeMusicAuthRequest
import ua.alxmute.migratemusic.auth.getResponse
import ua.alxmute.migratemusic.auth.openLoginActivity
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.mvp.activity.ChooseDirectoryActivity
import ua.alxmute.migratemusic.mvp.view.ChooseMusicServiceView

object ChooseMusicServicePresenter {

    private const val SPOTIFY_CLIENT_ID = "eff51993ba68441c92f1a1036ef2607e"
    private const val SPOTIFY_AUTH_TOKEN_REQUEST_CODE = 0x10

    private const val DEEZER_CLIENT_ID = "417902"
    private const val DEEZER_AUTH_TOKEN_REQUEST_CODE = 0x11

    private const val YOUTUBE_MUSIC_CLIENT_ID = "371716897364-c5igj0m29nh8vgn94bo0bgta78hhvrp1.apps.googleusercontent.com"
    private const val YOUTUBE_MUSIC_AUTH_TOKEN_REQUEST_CODE = 0x12


    lateinit var view: ChooseMusicServiceView

    fun onSpotifyLoginClick() {
        val redirectUri = Uri.Builder()
            .scheme(view.getString(R.string.com_spotify_sdk_redirect_scheme))
            .authority(view.getString(R.string.com_spotify_sdk_redirect_host))
            .build()

        val request = AuthorizationRequest.Builder(
            SPOTIFY_CLIENT_ID,
            AuthorizationResponse.Type.TOKEN,
            redirectUri.toString()
        )
            .setShowDialog(false)
            .setScopes(arrayOf("user-read-email", "user-library-modify"))
            .setCampaign("your-campaign-token")
            .build()

        AuthorizationClient.openLoginActivity(
            view.getActivity(),
            SPOTIFY_AUTH_TOKEN_REQUEST_CODE,
            request
        )
    }

    fun onLoginActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SPOTIFY_AUTH_TOKEN_REQUEST_CODE -> {
                val response = AuthorizationClient.getResponse(resultCode, data)
                if (response.type != AuthorizationResponse.Type.EMPTY) {
                    ContextHolder.token = response.accessToken
                    ContextHolder.musicServiceName = MusicServiceName.SPOTIFY
                    startChooseDirectoryActivity()
                }
            }
            DEEZER_AUTH_TOKEN_REQUEST_CODE -> {
                val response = AuthClient.getResponse(resultCode, data)
                if (response.type != AuthResponse.Type.EMPTY) {
                    ContextHolder.token = response.token!!
                    ContextHolder.musicServiceName = MusicServiceName.DEEZER
                    startChooseDirectoryActivity()
                }
            }
            YOUTUBE_MUSIC_AUTH_TOKEN_REQUEST_CODE -> {
                val response = AuthClient.getResponse(resultCode, data)
                if (response.type != AuthResponse.Type.EMPTY) {
                    ContextHolder.token = response.token!!
                    ContextHolder.musicServiceName = MusicServiceName.YOUTUBE_MUSIC
                    startChooseDirectoryActivity()
                }
            }
        }
    }

    fun onDeezerLoginClick() {
        val authRequest = DeezerAuthRequest(
            DEEZER_CLIENT_ID,
            "https://callback",
            "manage_library"
        )

        AuthClient.openLoginActivity(
            view.getActivity(),
            DEEZER_AUTH_TOKEN_REQUEST_CODE,
            authRequest
        )
    }

    fun onYoutubeMusicLoginClick() {
        val authRequest = YoutubeMusicAuthRequest(
            YOUTUBE_MUSIC_CLIENT_ID,
            "http://migratemusic-callback.com",
            "https://www.googleapis.com/auth/youtube"
        )
        AuthClient.openLoginActivity(
            view.getActivity(),
            YOUTUBE_MUSIC_AUTH_TOKEN_REQUEST_CODE,
            authRequest
        )
    }

    private fun startChooseDirectoryActivity() {
        view.getActivity().apply {
            startActivity(Intent(this, ChooseDirectoryActivity::class.java))
        }
    }

}