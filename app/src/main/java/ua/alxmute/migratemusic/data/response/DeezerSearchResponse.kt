package ua.alxmute.migratemusic.data.response

data class DeezerSearchResponse(
    val total: Int,
    val data: List<TrackData>
) {
    data class TrackData(
        val id: String,
        val title: String,
        val artist: ArtistResponse
    ) {
        data class ArtistResponse(
            val name: String
        )
    }
}