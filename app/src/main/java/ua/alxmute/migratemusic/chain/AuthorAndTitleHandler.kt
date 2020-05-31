package ua.alxmute.migratemusic.chain

import ua.alxmute.migratemusic.data.LocalTrackDto

class AuthorAndTitleHandler : AddTrackChain() {

    override fun getQueryString(localTrackDto: LocalTrackDto): String {
        return "${localTrackDto.author.orEmpty()} ${localTrackDto.title.orEmpty()}"
    }

}