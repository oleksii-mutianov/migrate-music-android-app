package ua.alxmute.migratemusic.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_file_processing.*
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.adapter.TrackRecyclerViewAdapter
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.LocalTrackDto
import ua.alxmute.migratemusic.service.DirectoryProcessor
import ua.alxmute.migratemusic.service.MusicProcessorService
import javax.inject.Inject
import kotlin.concurrent.thread

class FileProcessingActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var contextHolder: ContextHolder

    @Inject
    lateinit var directoryProcessor: DirectoryProcessor

    @Inject
    lateinit var musicProcessorService: MusicProcessorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_processing)

        textDirectory.text = contextHolder.directory

        val tracksToProcess = directoryProcessor.getMusicFromDirectory(contextHolder.directory)

        val processedTracks = ArrayList<LocalTrackDto>()
        val trackRecyclerViewAdapter = TrackRecyclerViewAdapter(processedTracks, this)

        rcProcessedTracks.adapter = trackRecyclerViewAdapter
        rcProcessedTracks.layoutManager = LinearLayoutManager(this)

        thread {
            musicProcessorService.addTracks(tracksToProcess, processedTracks, trackRecyclerViewAdapter)
        }

    }
}
