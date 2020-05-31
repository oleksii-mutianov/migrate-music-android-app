package ua.alxmute.migratemusic.di.module

import android.media.MediaMetadataRetriever
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DirectoryProcessorModule {

    @Provides
    @Singleton
    fun mediaMetadataRetriever(): MediaMetadataRetriever = MediaMetadataRetriever()
}