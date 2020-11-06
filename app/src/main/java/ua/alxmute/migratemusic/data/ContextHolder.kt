package ua.alxmute.migratemusic.data

import android.content.Context
import android.support.v4.provider.DocumentFile

object ContextHolder {
    var token: String = ""
    var directory: DocumentFile? = null
    var directoryContext: Context? = null
    var musicServiceName: MusicServiceName = MusicServiceName.SPOTIFY
}