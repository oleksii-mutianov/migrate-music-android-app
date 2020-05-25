package ua.alxmute.migratemusic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import net.rdrei.android.dirchooser.DirectoryChooserFragment

class MainActivity : AppCompatActivity(), DirectoryChooserFragment.OnFragmentInteractionListener {

    companion object {
        private val PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        private const val READ_STORAGE_PERMISSION = 1
    }

    private val chooserFragment: DirectoryChooserFragment = DirectoryChooserFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, READ_STORAGE_PERMISSION)
        }

        btnChoose.setOnClickListener {
            chooserFragment.show(supportFragmentManager, null)
        }
    }

    override fun onSelectDirectory(path: String) {
        textDirectory.text = path
        chooserFragment.dismiss()
    }

    override fun onCancelChooser() {
        chooserFragment.dismiss()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            READ_STORAGE_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                            this, "Permission granted! Please click on pick a file once again.",
                            Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                            this, "Permission denied to read your External storage :(",
                            Toast.LENGTH_SHORT
                    ).show()
                    ActivityCompat.requestPermissions(this, PERMISSIONS, READ_STORAGE_PERMISSION)
                }
                return
            }
        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}
