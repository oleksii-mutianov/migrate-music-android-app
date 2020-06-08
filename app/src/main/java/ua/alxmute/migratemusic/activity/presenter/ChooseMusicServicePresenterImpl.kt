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
import ua.alxmute.migratemusic.auth.youtube.YoutubeMusicAuthClient
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.service.LoginListener

class ChooseMusicServicePresenterImpl(
    val view: ChooseMusicServiceView,
    val contextHolder: ContextHolder,
    val deezerAuthClient: DeezerAuthClient
) : ChooseMusicServicePresenter, LoginListener {

    companion object {
        const val SPOTIFY_CLIENT_ID = "eff51993ba68441c92f1a1036ef2607e"
        const val SPOTIFY_AUTH_TOKEN_REQUEST_CODE = 0x10

        const val DEEZER_CLIENT_ID = "417902"

        const val YOUTUBE_MUSIC_CLIENT_ID = "371716897364-c5igj0m29nh8vgn94bo0bgta78hhvrp1.apps.googleusercontent.com"
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
                startChooseDirectoryActivity()
            }
        }
    }

    override fun onDeezerLoginClick() = LoginDialog(
        view.getActivity(),
        "https://connect.deezer.com/oauth/auth.php?app_id=$DEEZER_CLIENT_ID&redirect_uri=https://callback&perms=manage_library",
        deezerAuthClient,
        this
    ).show()

    override fun onYoutubeMusicLoginClick() = LoginDialog(
        view.getActivity(),
        "https://accounts.google.com/o/oauth2/auth?response_type=token&client_id=$YOUTUBE_MUSIC_CLIENT_ID" +
                "&scope=https://www.googleapis.com/auth/youtube&redirect_uri=http://migratemusic-callback.com",
        YoutubeMusicAuthClient(),
        this
    ).show()

    override fun onLoggedIn(accessToken: String, musicServiceName: MusicServiceName) {
        contextHolder.token = accessToken
        contextHolder.musicServiceName = musicServiceName
        startChooseDirectoryActivity()
    }

    private fun startChooseDirectoryActivity() {
        val activity = view.getActivity()
        activity.startActivity(Intent(activity, ChooseDirectoryActivity::class.java))
    }
}