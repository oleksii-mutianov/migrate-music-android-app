package ua.alxmute.migratemusic.media

import android.support.v4.provider.DocumentFile
import ua.alxmute.migratemusic.data.LocalTrackDto

val DocumentFile?.music: List<LocalTrackDto>
    get() = this?.allFilesIn()?.filter(DocumentFile::isMediaFile)?.map(DocumentFile::toLocalTrackDto).orEmpty()

val DocumentFile?.musicCount: Int
    get() = this?.allFilesIn()?.count(DocumentFile::isMediaFile) ?: 0

val DocumentFile.isMediaFile
    get() = uri.toString().substringAfterLast('.') == "mp3"

fun DocumentFile.allFilesIn(): List<DocumentFile> = when {
    isDirectory -> listFiles().flatMap { it.allFilesIn() }
    isFile -> listOf(this)
    else -> emptyList()
}

fun DocumentFile.toLocalTrackDto(): LocalTrackDto {
    val mediaMetadata = MediaMetadata(this)

    val author = mediaMetadata.author
    val title = mediaMetadata.title

    return LocalTrackDto(author, title, name!!)
}
