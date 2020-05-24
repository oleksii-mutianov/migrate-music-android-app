package ua.alxmute.migratemusic

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_file_processing.*
import ua.alxmute.migratemusic.service.FileWalker
import java.io.File

class FileProcessingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_processing)

        var path = intent.getStringExtra("path")

        textDirectory.text = path
        FileWalker().walk(File(path))

    }
}
