package ua.alxmute.migratemusic.mvp.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.provider.DocumentFile
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_choose_directory.*
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.media.musicCount

class ChooseDirectoryActivity : AppCompatActivity() {

    companion object {
        private val PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        private const val READ_STORAGE_PERMISSION = 1
        private const val CHOOSE_DIR_REQ_CODE = 9999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_directory)

        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, READ_STORAGE_PERMISSION)
        }

        btnChoose.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            startActivityForResult(
                Intent.createChooser(intent, "Choose directory"),
                CHOOSE_DIR_REQ_CODE
            )
        }

        btnConfirm.setOnClickListener {
            startActivity(Intent(this, FileProcessingActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CHOOSE_DIR_REQ_CODE) {

            data?.data?.let { path ->
                ContextHolder.directoryContext = this
                ContextHolder.directory = DocumentFile.fromTreeUri(this, path)
                val musicCount = ContextHolder.directory.musicCount

                textDirectory.text = path.path
                trackFoundCounter.text = resources.getString(R.string.track_counter, musicCount)
                btnConfirm.isEnabled = musicCount > 0
            }
        }
    }

    private fun hasPermissions(): Boolean {
        return PERMISSIONS.all {
            ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}