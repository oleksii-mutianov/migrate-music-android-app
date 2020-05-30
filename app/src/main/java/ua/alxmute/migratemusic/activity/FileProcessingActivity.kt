package ua.alxmute.migratemusic.activity

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_file_processing.*
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.service.FileWalker
import java.io.File
import javax.inject.Inject

class FileProcessingActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var contextHolder: ContextHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_processing)

        textDirectory.text = contextHolder.directory
        FileWalker().walk(File(contextHolder.directory))

    }
}
