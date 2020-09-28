package ua.alxmute.migratemusic.mvp.presenter

import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.media.music
import ua.alxmute.migratemusic.mvp.view.FileProcessingView
import ua.alxmute.migratemusic.service.MusicProcessorService
import ua.alxmute.migratemusic.service.listener.TrackProcessingListener

object FileProcessingPresenter : TrackProcessingListener {

    var view: FileProcessingView? = null

    fun onLoad() {
        view?.let {
            val tracksToProcess = ContextHolder.directory.music
            MusicProcessorService.addTracks(tracksToProcess, it.processedTracks, this)
        }
    }

    override fun onTrackProcessed(processedSize: Int, totalSize: Int) {
        view?.let {
            it.setTrackCounter(
                it.getResources().getString(R.string.processed_tracks, processedSize, totalSize)
            )
            it.refreshList()
        }
    }

}