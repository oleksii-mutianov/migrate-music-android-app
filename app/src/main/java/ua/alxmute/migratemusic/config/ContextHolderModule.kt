package ua.alxmute.migratemusic.config

import dagger.Module
import dagger.Provides
import ua.alxmute.migratemusic.data.ContextHolder
import javax.inject.Singleton

@Module
class ContextHolderModule {

    @Provides
    @Singleton
    fun contextHolder(): ContextHolder = ContextHolder()

}