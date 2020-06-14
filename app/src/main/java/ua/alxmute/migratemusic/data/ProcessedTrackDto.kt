package ua.alxmute.migratemusic.data

data class ProcessedTrackDto(
    val author: String?,
    val title: String?,
    val fileName: String,
    val isFailed: Boolean
)