package ua.alxmute.migratemusic.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.MusicServiceName
import javax.inject.Inject

class ChooseMusicServiceActivity : DaggerAppCompatActivity() {

    companion object {
        const val CLIENT_ID = "eff51993ba68441c92f1a1036ef2607e"
        const val AUTH_TOKEN_REQUEST_CODE = 0x10
    }

    @Inject
    lateinit var contextHolder: ContextHolder

    private val redirectUri: Uri by lazy {
        Uri.Builder()
            .scheme(getString(R.string.com_spotify_sdk_redirect_scheme))
            .authority(getString(R.string.com_spotify_sdk_redirect_host))
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_music_service)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthenticationClient.getResponse(resultCode, data)

        if (response.type != AuthenticationResponse.Type.EMPTY) {
            if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
                contextHolder.token = response.accessToken
                contextHolder.musicServiceName = MusicServiceName.SPOTIFY
            }

            startActivity(Intent(this, ChooseDirectoryActivity::class.java))
        }
    }

    fun onSpotifyLoginClick(view: View) {
        val request = getAuthenticationRequest(AuthenticationResponse.Type.TOKEN)
        AuthenticationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request)
    }

//    fun requestTrack(view: View) {
//        val trackName = "linkin park numb"
//
//        thread {
//            val track = spotifyService.requestTrack(trackName)
//            setResponse("${track.artists[0].name} - ${track.name} (${track.id})")
//        }
//
//    }

//    fun addTrack(view: View) {
//        val trackId = "4NjcBu9yymywoxK1l9tdBE"
//        thread {
//            spotifyService.addTrack(trackId)
//        }
//    }

//    private fun setResponse(text: String) {
//        runOnUiThread {
//            profileTextView.text = text
//        }
//    }

    private fun getAuthenticationRequest(type: AuthenticationResponse.Type): AuthenticationRequest {
        return AuthenticationRequest.Builder(CLIENT_ID, type, redirectUri.toString())
            .setShowDialog(false)
            .setScopes(arrayOf("user-read-email", "user-library-modify"))
            .setCampaign("your-campaign-token")
            .build()
    }

}
