package ua.alxmute.auth.data

import android.net.Uri
import ua.alxmute.auth.constants.*
import java.io.Serializable

abstract class AuthRequest : Serializable {

    private val scheme: String = "https"
    protected abstract val authority: String
    protected abstract val path: String

    protected abstract val uriParams: Uri.Builder.() -> Uri.Builder

    fun toUri(): Uri {
        return Uri.Builder()
            .scheme(scheme)
            .authority(authority)
            .path(path)
            .uriParams()
            .build()
    }
}


class DeezerAuthRequest(
    appId: String,
    redirectUri: String,
    vararg scopes: String,
) : AuthRequest() {

    override val authority = "connect.deezer.com"
    override val path = "oauth/auth.php"

    override val uriParams: Uri.Builder.() -> Uri.Builder = {
        this.appendQueryParameter(APP_ID, appId)
            .appendQueryParameter(REDIRECT_URI, redirectUri)
            .appendQueryParameter(PERMS, scopes.joinToString())
    }
}


class YoutubeMusicAuthRequest(
    appId: String,
    redirectUri: String,
    vararg scopes: String,
) : AuthRequest() {

    override val authority = "accounts.google.com"
    override val path = "o/oauth2/auth"

    override val uriParams: Uri.Builder.() -> Uri.Builder = {
        this.appendQueryParameter(CLIENT_ID, appId)
            .appendQueryParameter(REDIRECT_URI, redirectUri)
            .appendQueryParameter(SCOPE, scopes.joinToString())
            .appendQueryParameter(RESPONSE_TYPE, "token")
    }
}