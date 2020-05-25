package ua.alxmute.migratemusic.service

import android.media.MediaMetadataRetriever
import android.util.Log
import java.io.File

class FileWalker {

    private val replacementRegex = Regex("\\(.*\\)|\\[.*]")
    private val mediaMetadataRetriever = MediaMetadataRetriever()

    fun walk(root: File) {

        val files = ArrayList<File>()

        for (f in root.listFiles()!!) {
            if (f.isDirectory) {
                walk(f)
            } else {
                val name = f.absolutePath
                if (name.substringAfterLast('.') == "mp3") {
                    mediaMetadataRetriever.setDataSource(name)

                    val author =
                        mediaMetadataRetriever
                            .extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                            .replace(replacementRegex, "")
                            .trim()

                    val title =
                        mediaMetadataRetriever
                            .extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                            .replace(replacementRegex, "")
                            .trim()

                    Log.i("song", "$author - $title")

                    files.add(f)
                }
            }
        }

        files.forEach {
            Log.i("", it.absoluteFile.absolutePath)
        }
    }

}