package ua.alxmute.migratemusic.di.module

import dagger.Module
import dagger.Provides
import ua.alxmute.migratemusic.chain.AddTrackChain
import ua.alxmute.migratemusic.chain.AuthorAndTitleHandler
import ua.alxmute.migratemusic.chain.AuthorAndTitleRegexHandler
import ua.alxmute.migratemusic.chain.FilenameHandler
import javax.inject.Singleton

@Module
class AddTrackChainModule {

    @Provides
    @Singleton
    fun addTrackChain(): AddTrackChain {
        val chain: AddTrackChain = AuthorAndTitleHandler()

        chain
            .setNext(AuthorAndTitleRegexHandler())
            .setNext(FilenameHandler())

        return chain
    }

}