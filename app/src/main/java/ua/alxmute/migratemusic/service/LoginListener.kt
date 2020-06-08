package ua.alxmute.migratemusic.service

import ua.alxmute.migratemusic.data.MusicServiceName

interface LoginListener {

    fun onLoggedIn(accessToken: String, musicServiceName: MusicServiceName)

}