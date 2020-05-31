package ua.alxmute.migratemusic.di.module;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ua.alxmute.migratemusic.activity.FileProcessingActivity;
import ua.alxmute.migratemusic.activity.presenter.FileProcessingPresenter;
import ua.alxmute.migratemusic.activity.presenter.FileProcessingPresenterImpl;
import ua.alxmute.migratemusic.activity.view.FileProcessingView;
import ua.alxmute.migratemusic.data.ContextHolder;
import ua.alxmute.migratemusic.service.DirectoryProcessor;
import ua.alxmute.migratemusic.service.MusicProcessorService;

@Module
public abstract class FileProcessingActivityModule {

    @Provides
    static FileProcessingPresenter fileProcessingPresenter(
            FileProcessingView view,
            ContextHolder contextHolder,
            DirectoryProcessor directoryProcessor,
            MusicProcessorService musicProcessorService
    ) {
        return new FileProcessingPresenterImpl(view, contextHolder, directoryProcessor, musicProcessorService);
    }

    @Binds
    abstract FileProcessingView fileProcessingView(FileProcessingActivity fileProcessingActivity);

}
