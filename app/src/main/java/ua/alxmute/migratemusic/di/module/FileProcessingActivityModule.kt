package ua.alxmute.migratemusic.di.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import ua.alxmute.migratemusic.activity.FileProcessingActivity
import ua.alxmute.migratemusic.activity.presenter.FileProcessingPresenter
import ua.alxmute.migratemusic.activity.presenter.FileProcessingPresenterImpl
import ua.alxmute.migratemusic.activity.view.FileProcessingView
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.service.DirectoryProcessor
import ua.alxmute.migratemusic.service.MusicProcessorService

@Module
abstract class FileProcessingActivityModule {

    @Binds
    abstract fun fileProcessingView(fileProcessingActivity: FileProcessingActivity): FileProcessingView

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun fileProcessingPresenter(
            view: FileProcessingView,
            contextHolder: ContextHolder,
            directoryProcessor: DirectoryProcessor,
            musicProcessorService: MusicProcessorService
        ): FileProcessingPresenter = FileProcessingPresenterImpl(view, contextHolder, directoryProcessor, musicProcessorService)
    }

}