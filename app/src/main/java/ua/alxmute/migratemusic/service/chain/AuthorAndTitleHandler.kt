package ua.alxmute.migratemusic.service.chain

import ua.alxmute.migratemusic.data.LocalTrackDto

object AuthorAndTitleHandler : AddTrackChain() {

    override fun getQueryString(localTrackDto: LocalTrackDto): String {
        return "${localTrackDto.author.orEmpty()} ${localTrackDto.title.orEmpty()}"
    }

}