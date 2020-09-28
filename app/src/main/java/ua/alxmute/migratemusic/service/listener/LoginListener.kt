package ua.alxmute.migratemusic.service.listener

import ua.alxmute.migratemusic.data.MusicServiceName

interface LoginListener {

    fun onLoggedIn(accessToken: String, musicServiceName: MusicServiceName)

}