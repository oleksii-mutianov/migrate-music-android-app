package ua.alxmute.migratemusic.auth

import ua.alxmute.migratemusic.service.LoginListener

interface AuthClient {

    fun onComplete(code: String, loginListener: LoginListener)

}