package ua.alxmute.migratemusic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.android.synthetic.main.activity_spotify_login.*
import ua.alxmute.migratemusic.config.DaggerAppComponent
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.service.SpotifyService
import javax.inject.Inject
import kotlin.concurrent.thread

class SpotifyLoginActivity : AppCompatActivity() {

    companion object {
        const val CLIENT_ID = "eff51993ba68441c92f1a1036ef2607e"
        const val AUTH_TOKEN_REQUEST_CODE = 0x10
    }

    @Inject
    lateinit var contextHolder: ContextHolder

    @Inject
    lateinit var spotifyService: SpotifyService

    private val redirectUri: Uri by lazy {
        Uri.Builder()
            .scheme(getString(R.string.com_spotify_sdk_redirect_scheme))
            .authority(getString(R.string.com_spotify_sdk_redirect_host))
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify_login)

        DaggerAppComponent.create().inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthenticationClient.getResponse(resultCode, data)

        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            contextHolder.token = response.accessToken
            updateTokenView()
        }
    }

    fun onSpotifyLoginClick(view: View) {
        val request = getAuthenticationRequest(AuthenticationResponse.Type.TOKEN)
        AuthenticationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request)
    }

    fun requestTrack(view: View) {
        val trackName = "linkin park numb"

        thread {
            val track = spotifyService.requestTrack(trackName)
            setResponse("${track.artists[0].name} - ${track.name} (${track.id})")
        }

    }

    fun addTrack(view: View) {
        val trackId = "4NjcBu9yymywoxK1l9tdBE"
        thread {
            spotifyService.addTrack(trackId)
        }
    }

    private fun updateTokenView() {
        tokenTextView.text = getString(R.string.token, contextHolder.token)
    }

    private fun setResponse(text: String) {
        runOnUiThread {
            profileTextView.text = text
        }
    }

    private fun getAuthenticationRequest(type: AuthenticationResponse.Type): AuthenticationRequest {
        return AuthenticationRequest.Builder(CLIENT_ID, type, redirectUri.toString())
            .setShowDialog(false)
            .setScopes(arrayOf("user-read-email", "user-library-modify"))
            .setCampaign("your-campaign-token")
            .build()
    }

}
