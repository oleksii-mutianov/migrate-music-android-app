package ua.alxmute.migratemusic.auth.youtube

import ua.alxmute.migratemusic.auth.AuthClient
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.service.LoginListener

class YoutubeMusicAuthClient : AuthClient {
    override fun onComplete(code: String, loginListener: LoginListener) {
        loginListener.onLoggedIn(code, MusicServiceName.YOUTUBE_MUSIC)
    }
}