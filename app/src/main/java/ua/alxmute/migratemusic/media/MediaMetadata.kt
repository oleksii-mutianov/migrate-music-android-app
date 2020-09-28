package ua.alxmute.migratemusic.media

import android.media.MediaMetadataRetriever
import android.support.v4.provider.DocumentFile
import ua.alxmute.migratemusic.data.ContextHolder

class MediaMetadata(file: DocumentFile) {

    private val mediaMetadataRetriever =
        MediaMetadataRetriever().apply { setDataSource(ContextHolder.directoryContext, file.uri) }

    val author: String?
        get() = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

    val title: String?
        get() = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)

}