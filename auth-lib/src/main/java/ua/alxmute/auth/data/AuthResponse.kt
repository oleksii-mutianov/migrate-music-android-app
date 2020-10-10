package ua.alxmute.auth.data

import android.net.Uri
import ua.alxmute.auth.constants.ACCESS_TOKEN
import ua.alxmute.auth.constants.CODE
import ua.alxmute.auth.constants.DEEZER
import ua.alxmute.auth.constants.YOUTUBE
import java.io.Serializable

open class AuthResponse @JvmOverloads constructor(
    val type: Type,
    val error: String? = null,
    val code: String? = null,
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
                code = uri.fragment?.substringAfter("$ACCESS_TOKEN=")?.substringBefore("&")
            )
            else -> throw RuntimeException("It's not working :(")
        }
    }
}