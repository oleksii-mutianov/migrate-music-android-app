package ua.alxmute.migratemusic.auth.deezer

import ua.alxmute.migratemusic.service.LoginListener

interface AuthClient {

    fun onComplete(code: String, loginListener: LoginListener)

}