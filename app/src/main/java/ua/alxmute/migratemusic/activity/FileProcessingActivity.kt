package ua.alxmute.migratemusic.activity

import android.os.Bundle
import android.util.Log
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_file_processing.*
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.service.DirectoryProcessor
import javax.inject.Inject

class FileProcessingActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var contextHolder: ContextHolder

    @Inject
    lateinit var directoryProcessor: DirectoryProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_processing)

        textDirectory.text = contextHolder.directory

        val tracks = directoryProcessor.getMusicFromDirectory(contextHolder.directory)

        Log.i("list of tracks", tracks.toString())

    }
}
