package ua.alxmute.migratemusic.di.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import ua.alxmute.migratemusic.activity.ChooseMusicServiceActivity
import ua.alxmute.migratemusic.activity.FileProcessingActivity
import ua.alxmute.migratemusic.activity.presenter.ChooseMusicServicePresenter
import ua.alxmute.migratemusic.activity.presenter.ChooseMusicServicePresenterImpl
import ua.alxmute.migratemusic.activity.presenter.FileProcessingPresenter
import ua.alxmute.migratemusic.activity.presenter.FileProcessingPresenterImpl
import ua.alxmute.migratemusic.activity.view.ChooseMusicServiceView
import ua.alxmute.migratemusic.activity.view.FileProcessingView
import ua.alxmute.migratemusic.auth.AuthClient
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.service.DirectoryProcessor
import ua.alxmute.migratemusic.service.MusicProcessorService

@Module
abstract class ActivityModule {

    @Binds
    abstract fun fileProcessingView(fileProcessingActivity: FileProcessingActivity): FileProcessingView

    @Binds
    abstract fun chooseMusicServiceView(chooseMusicServiceView: ChooseMusicServiceActivity): ChooseMusicServiceView

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

        @JvmSuppressWildcards
        @JvmStatic
        @Provides
        fun chooseMusicServicePresenter(
            chooseMusicServiceView: ChooseMusicServiceView,
            contextHolder: ContextHolder,
            authClients: Map<MusicServiceName, AuthClient>
        ): ChooseMusicServicePresenter = ChooseMusicServicePresenterImpl(chooseMusicServiceView, contextHolder, authClients)
    }

}