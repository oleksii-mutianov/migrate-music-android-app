package ua.alxmute.migratemusic.service

interface DeezerLoginListener {

    fun onDeezerLoggedIn(accessToken: String)

}