package ua.alxmute.migratemusic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.gson.Gson
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.android.synthetic.main.activity_spotify_login.*
import okhttp3.*
import ua.alxmute.migratemusic.data.SearchResponse
import java.io.IOException

class SpotifyLoginActivity : AppCompatActivity() {

    companion object {
        const val CLIENT_ID = "eff51993ba68441c92f1a1036ef2607e"
        const val AUTH_TOKEN_REQUEST_CODE = 0x10
        const val AUTH_CODE_REQUEST_CODE = 0x11
    }

    private val httpClient = OkHttpClient()
    private val gson = Gson()

    private var accessToken: String? = null
    private var accessCode: String? = null
    private var mCall: Call? = null

    private val redirectUri: Uri by lazy {
        Uri.Builder()
            .scheme(getString(R.string.com_spotify_sdk_redirect_scheme))
            .authority(getString(R.string.com_spotify_sdk_redirect_host))
            .build()
    }

    private val spotifyHttpCallback by lazy {
        object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                setResponse("Failed to fetch data: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                val result = gson.fromJson(response.body()!!.string(), SearchResponse::class.java).tracks
                if (result.total > 0) {
                    val track = result.items[0]
                    setResponse("${track.artists[0].name} - ${track.name} (${track.id})")
                } else {
                    setResponse("no results :(")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify_login)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthenticationClient.getResponse(resultCode, data)

        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            accessToken = response.accessToken
            updateTokenView()
        } else if (AUTH_CODE_REQUEST_CODE == requestCode) {
            accessCode = response.code
            updateCodeView()
        }
    }

    override fun onDestroy() {
        cancelCall()
        super.onDestroy()
    }

    fun onSpotifyLoginClick(view: View) {
        val request = getAuthenticationRequest(AuthenticationResponse.Type.TOKEN)
        AuthenticationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request)
    }

    fun requestTrack(view: View) {

        val trackName = "linkin park numb".replace(" ", "+")

        val searchTrackRequest = Request.Builder()
            .url("https://api.spotify.com/v1/search?type=track&q=$trackName&limit=1")
            .addHeader("Authorization", "Bearer $accessToken")
            .get()
            .build()

        cancelCall() // TODO: ???

        mCall = httpClient.newCall(searchTrackRequest)
        mCall?.enqueue(spotifyHttpCallback)

    }

    fun addTrack(view: View) {
        val trackId = "4NjcBu9yymywoxK1l9tdBE"
        val addTrackRequest = Request.Builder()
            .url("https://api.spotify.com/v1/me/tracks?ids=$trackId")
            .addHeader("Authorization", "Bearer $accessToken")
            .get()
            .build()

        mCall = httpClient.newCall(addTrackRequest)
    }

    private fun updateTokenView() {
        tokenTextView.text = getString(R.string.token, accessToken)
    }

    private fun updateCodeView() {
        codeTextView.text = getString(R.string.code, accessCode)
    }

    private fun setResponse(text: String) {
        runOnUiThread {
            profileTextView.text = text
        }
    }

    private fun cancelCall() {
        mCall?.cancel()
    }

    private fun getAuthenticationRequest(type: AuthenticationResponse.Type): AuthenticationRequest {
        return AuthenticationRequest.Builder(CLIENT_ID, type, redirectUri.toString())
            .setShowDialog(false)
            .setScopes(arrayOf("user-read-email", "user-library-modify"))
            .setCampaign("your-campaign-token")
            .build()
    }

}
