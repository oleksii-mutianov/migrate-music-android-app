package ua.alxmute.migratemusic.mvp.presenter

import android.content.Intent
import android.net.Uri
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.mvp.activity.ChooseDirectoryActivity
import ua.alxmute.migratemusic.mvp.view.ChooseMusicServiceView
import ua.alxmute.migratemusic.service.listener.LoginListener

object ChooseMusicServicePresenter : LoginListener {

    private const val SPOTIFY_CLIENT_ID = "eff51993ba68441c92f1a1036ef2607e"
    private const val SPOTIFY_AUTH_TOKEN_REQUEST_CODE = 0x10

    var view: ChooseMusicServiceView? = null

    fun onSpotifyLoginClick() {
        view?.let {
            val redirectUri = Uri.Builder()
                .scheme(it.getString(R.string.com_spotify_sdk_redirect_scheme))
                .authority(it.getString(R.string.com_spotify_sdk_redirect_host))
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
                it.getActivity(),
                SPOTIFY_AUTH_TOKEN_REQUEST_CODE,
                request
            )
        }
    }

    fun onSpotifyActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SPOTIFY_AUTH_TOKEN_REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            if (response.type != AuthorizationResponse.Type.EMPTY) {
                ContextHolder.token = response.accessToken
                ContextHolder.musicServiceName = MusicServiceName.SPOTIFY
                startChooseDirectoryActivity()
            }
        }
    }

    fun onDeezerLoginClick() {
        TODO("Not yet implemented")
    }

    fun onYoutubeMusicLoginClick() {
        TODO("Not yet implemented")
    }

    override fun onLoggedIn(accessToken: String, musicServiceName: MusicServiceName) {
        TODO("Not yet implemented")
    }

    private fun startChooseDirectoryActivity() {
        view?.getActivity()?.let {
            it.startActivity(Intent(it, ChooseDirectoryActivity::class.java))
        }
    }

}