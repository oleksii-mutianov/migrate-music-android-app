package ua.alxmute.migratemusic.auth.deezer

import ua.alxmute.migratemusic.service.DeezerLoginListener

interface AuthClient {

    fun onComplete(code: String, loginListener: DeezerLoginListener)

}