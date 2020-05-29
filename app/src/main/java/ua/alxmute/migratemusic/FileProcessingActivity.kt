package ua.alxmute.migratemusic

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.service.FileWalker
import java.io.File
import javax.inject.Inject

class FileProcessingActivity : AppCompatActivity() {

    @Inject
    lateinit var contextHolder: ContextHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_processing)

        FileWalker().walk(File(contextHolder.directory))

    }
}
