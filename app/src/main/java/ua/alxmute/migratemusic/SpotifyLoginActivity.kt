package ua.alxmute.migratemusic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

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
                val result = objectMapper.readValue(response.body()!!.string(), SearchResponse::class.java).tracks
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

        val request = Request.Builder()
            .url("https://api.spotify.com/v1/search?type=track&q=$trackName&limit=1")
            .addHeader("Authorization", "Bearer $accessToken")
            .get()
            .build()

//        val trackId = "0VjIjW4GlUZAMYd2vXMi3b"
//        val requestAddTrack = Request.Builder()
//            .url("https://spclient.wg.spotify.com/collection-view/v1/collection/tracks/2164gakoiseo3txu67vedncmy?base62ids=$trackId&model=bookmark")
//            .addHeader("Authorization", "Bearer $mAccessToken")
//            .get()
//            .build()

        cancelCall()

        mCall = httpClient.newCall(request)
        mCall?.enqueue(spotifyHttpCallback)

    }

    private fun updateTokenView() {
        tokenTextView.text = getString(R.string.token, accessToken)
    }

    private fun updateCodeView() {
        codeTextView.text = getString(R.string.code, accessCode)
    }

//    private fun requestUserProfile() {
//        val request = Request.Builder()
//            .url("https://api.spotify.com/v1/me")
//            .addHeader("Authorization", "Bearer $mAccessToken")
//            .build()
//
//        cancelCall()
//
//        mCall = httpClient.newCall(request)
//        mCall?.enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                setResponse("Failed to fetch data: $e")
//            }
//
//            @Throws(IOException::class)
//            override fun onResponse(call: Call, response: Response) {
//                try {
//                    val jsonObject = JSONObject(response.body()!!.string())
//                    setResponse(jsonObject.toString(3))
//                } catch (e: JSONException) {
//                    setResponse("Failed to parse data: $e")
//                }
//            }
//        })
//    }

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
            .setScopes(arrayOf("user-read-email"))
            .setCampaign("your-campaign-token")
            .build()
    }

}
