package ua.alxmute.migratemusic.service.chain

import ua.alxmute.migratemusic.data.LocalTrackDto

object FilenameHandler : AddTrackChain() {
    private val betweenBracketsAndParentheses = Regex("\\(.*\\)|\\[.*]")
    private val underscoreOrDash = Regex("[_\\-]")
    private val trailingSpaces = Regex(" +")

    override fun getQueryString(localTrackDto: LocalTrackDto): String {
        return if (localTrackDto.title.isNullOrBlank() and localTrackDto.author.isNullOrBlank()) {
            localTrackDto.fileName
                .substringBeforeLast('.')
                .replace(betweenBracketsAndParentheses, " ")
                .replace(underscoreOrDash, " ")
                .replace(trailingSpaces, " ")
                .trim()
        } else {
            ""
        }
    }
}