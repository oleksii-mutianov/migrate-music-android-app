package ua.alxmute.migratemusic.service.factory

import ua.alxmute.migratemusic.data.LocalTrackDto
import ua.alxmute.migratemusic.service.chain.AuthorAndTitleHandler
import ua.alxmute.migratemusic.service.chain.AuthorAndTitleRegexHandler
import ua.alxmute.migratemusic.service.chain.FilenameHandler
import ua.alxmute.migratemusic.service.strategy.MusicServiceStrategy

object AddTrackChainFactory {

    private val chain = AuthorAndTitleHandler.apply {
        this.setNext(AuthorAndTitleRegexHandler)
            .setNext(FilenameHandler)
    }

    fun handle(localTrackDto: LocalTrackDto, musicServiceStrategy: MusicServiceStrategy) =
        chain.handle(localTrackDto, musicServiceStrategy)

}