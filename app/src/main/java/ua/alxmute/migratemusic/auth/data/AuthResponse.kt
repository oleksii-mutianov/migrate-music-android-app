package ua.alxmute.migratemusic.auth.data

import android.net.Uri
import ua.alxmute.migratemusic.auth.constants.ACCESS_TOKEN
import ua.alxmute.migratemusic.auth.constants.CODE
import ua.alxmute.migratemusic.auth.constants.DEEZER
import ua.alxmute.migratemusic.auth.constants.YOUTUBE
import java.io.Serializable

open class AuthResponse(
    val type: Type,
    val error: String? = null,
    val code: String? = null,
    val token: String? = null,
) : Serializable {

    enum class Type {
        EMPTY,
        CODE,
        ERROR,
    }

    companion object {
        fun fromUri(uri: Uri) = when (uri.authority) {
            DEEZER -> AuthResponse(type = Type.CODE, code = uri.getQueryParameter(CODE))
            YOUTUBE -> AuthResponse(
                type = Type.CODE,
                token = uri.fragment?.substringAfter("$ACCESS_TOKEN=")?.substringBefore("&")
            )
            else -> throw RuntimeException("It's not working :(")
        }
    }
}