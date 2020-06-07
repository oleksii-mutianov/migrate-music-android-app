package ua.alxmute.migratemusic.activity.presenter

import android.content.Intent
import android.net.Uri
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.activity.ChooseDirectoryActivity
import ua.alxmute.migratemusic.activity.view.ChooseMusicServiceView
import ua.alxmute.migratemusic.auth.LoginDialog
import ua.alxmute.migratemusic.auth.deezer.DeezerAuthClient
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.service.DeezerLoginListener

class ChooseMusicServicePresenterImpl(
    val view: ChooseMusicServiceView,
    val contextHolder: ContextHolder,
    val deezerAuthClient: DeezerAuthClient
) : ChooseMusicServicePresenter, DeezerLoginListener {

    companion object {
        const val SPOTIFY_CLIENT_ID = "eff51993ba68441c92f1a1036ef2607e"
        const val SPOTIFY_AUTH_TOKEN_REQUEST_CODE = 0x10

        const val DEEZER_CLIENT_ID = "417902"
    }

    override fun onSpotifyLoginClick() {

        val redirectUri = Uri.Builder()
            .scheme(view.getString(R.string.com_spotify_sdk_redirect_scheme))
            .authority(view.getString(R.string.com_spotify_sdk_redirect_host))
            .build()

        val request = AuthenticationRequest.Builder(SPOTIFY_CLIENT_ID, AuthenticationResponse.Type.TOKEN, redirectUri.toString())
            .setShowDialog(false)
            .setScopes(arrayOf("user-read-email", "user-library-modify"))
            .setCampaign("your-campaign-token")
            .build()

        AuthenticationClient.openLoginActivity(view.getActivity(), SPOTIFY_AUTH_TOKEN_REQUEST_CODE, request)

    }

    override fun onSpotifyActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SPOTIFY_AUTH_TOKEN_REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, data)
            if (response.type != AuthenticationResponse.Type.EMPTY) {
                contextHolder.token = response.accessToken
                contextHolder.musicServiceName = MusicServiceName.SPOTIFY
            }
            startChooseDirectoryActivity()
        }
    }

    override fun onDeezerLoginClick() {
        LoginDialog(
            view.getActivity(),
            "https://connect.deezer.com/oauth/auth.php?app_id=$DEEZER_CLIENT_ID&redirect_uri=https://callback&perms=manage_library",
            deezerAuthClient,
            this
        ).show()
    }

    override fun onDeezerLoggedIn(accessToken: String) {
        contextHolder.token = accessToken
        contextHolder.musicServiceName = MusicServiceName.DEEZER
        startChooseDirectoryActivity()
    }

    private fun startChooseDirectoryActivity() {
        val activity = view.getActivity()
        activity.startActivity(Intent(activity, ChooseDirectoryActivity::class.java))
    }
}