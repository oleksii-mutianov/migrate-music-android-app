package ua.alxmute.migratemusic.activity.presenter

import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.activity.view.FileProcessingView
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.service.DirectoryProcessor
import ua.alxmute.migratemusic.service.MusicProcessorService
import ua.alxmute.migratemusic.service.TrackProcessingListener
import kotlin.concurrent.thread

class FileProcessingPresenterImpl(
    private val view: FileProcessingView,
    private val contextHolder: ContextHolder,
    private val directoryProcessor: DirectoryProcessor,
    private val musicProcessorService: MusicProcessorService
) : FileProcessingPresenter, TrackProcessingListener {

    override fun onload() {
        thread {
            view.setTextDirectory(contextHolder.directory)
            val tracksToProcess = directoryProcessor.getMusicFromDirectory(contextHolder.directory)
            musicProcessorService.addTracks(tracksToProcess, view.getListForProcessedTracks(), this)
        }
    }

    override fun onTrackProcessed(processedSize: Int, totalSize: Int) {
        view.setTrackCounter(
            view.getResources().getString(R.string.processed_tracks, processedSize, totalSize)
        )
        view.refreshList()
    }

}