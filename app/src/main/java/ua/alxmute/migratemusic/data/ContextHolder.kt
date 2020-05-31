package ua.alxmute.migratemusic.data

data class ContextHolder(
    var token: String = "",
    var directory: String = "",
    var musicServiceName: MusicServiceName = MusicServiceName.SPOTIFY
)