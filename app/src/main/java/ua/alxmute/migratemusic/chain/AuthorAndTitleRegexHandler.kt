package ua.alxmute.migratemusic.chain

import ua.alxmute.migratemusic.data.LocalTrackDto

class AuthorAndTitleRegexHandler : AddTrackChain() {

    private val replacementRegex = Regex("\\(.*\\)|\\[.*]")

    override fun getQueryString(localTrackDto: LocalTrackDto): String {
        val title = localTrackDto.title.orEmpty().replace(replacementRegex, "").trim()
        val author = localTrackDto.author.orEmpty().replace(replacementRegex, "").trim()

        return "$author $title"
    }
}