package ua.alxmute.migratemusic.media

import android.support.v4.provider.DocumentFile
import ua.alxmute.migratemusic.data.LocalTrackDto

val DocumentFile?.music: List<LocalTrackDto>
    get() {
        val tracks = mutableListOf<LocalTrackDto>()

        this?.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                tracks.addAll(file.music)
            } else if (file.isMediaFile) {
                tracks.add(file.toLocalTrackDto())
            }
        }
        return tracks
    }

val DocumentFile?.musicCount: Int
    get() {
        var counter = 0

        this?.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                counter += file.musicCount
            } else if (file.isMediaFile) {
                counter++
            }
        }

        return counter
    }

val DocumentFile.isMediaFile
    get() = uri.toString().substringAfterLast('.') == "mp3"

fun DocumentFile.toLocalTrackDto(): LocalTrackDto {
    val mediaMetadata = MediaMetadata(this)

    val author = mediaMetadata.author
    val title = mediaMetadata.title

    return LocalTrackDto(author, title, name!!)
}
