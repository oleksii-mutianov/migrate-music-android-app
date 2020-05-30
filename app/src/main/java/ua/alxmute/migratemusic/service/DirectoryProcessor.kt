package ua.alxmute.migratemusic.service

import android.media.MediaMetadataRetriever
import ua.alxmute.migratemusic.data.LocalTrackDto
import java.io.File
import javax.inject.Inject

class DirectoryProcessor
@Inject constructor(
    private val metadataRetriever: MediaMetadataRetriever
) {

    fun getMusicFromDirectory(directory: String): List<LocalTrackDto> {
        return getMusicFromDirectory(File(directory))
    }

    private fun getMusicFromDirectory(root: File): List<LocalTrackDto> {

        val tracks = ArrayList<LocalTrackDto>()

        for (file in root.listFiles()!!) {
            if (file.isDirectory) {
                getMusicFromDirectory(file)
            } else {
                val name = file.absolutePath
                if (name.substringAfterLast('.') == "mp3") {
                    metadataRetriever.setDataSource(name)

                    val author = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                    val title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)

                    tracks.add(LocalTrackDto(author, title, file.name))
                }
            }
        }

        return tracks
    }


}