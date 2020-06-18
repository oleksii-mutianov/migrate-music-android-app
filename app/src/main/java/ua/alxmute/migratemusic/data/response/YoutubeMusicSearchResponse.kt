package ua.alxmute.migratemusic.data.response

data class YoutubeMusicSearchResponse(
    val pageInfo: PageInfo,
    val items: List<Item>
) {
    data class PageInfo(
        val totalResults: Int
    )

    data class Item(
        val id: ItemId,
        val snippet: Snippet
    ) {
        data class ItemId(
            val videoId: String
        )

        data class Snippet(
            val title: String
        )
    }
}