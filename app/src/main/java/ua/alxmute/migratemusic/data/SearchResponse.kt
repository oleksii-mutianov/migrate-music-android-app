package ua.alxmute.migratemusic.data

data class SearchResponse(
    val tracks: TracksWrapper
) {

    data class TracksWrapper(
        val total: Int,
        val items: List<TrackResponse>
    ) {

        data class TrackResponse(
            val id: String,
            val name: String,
            val artists: List<ArtistResponse>

        ) {

            data class ArtistResponse(
                val name: String
            )

        }
    }
}