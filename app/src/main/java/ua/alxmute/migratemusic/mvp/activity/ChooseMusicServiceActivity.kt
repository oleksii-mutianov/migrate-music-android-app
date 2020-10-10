package ua.alxmute.migratemusic.mvp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.mvp.presenter.ChooseMusicServicePresenter
import ua.alxmute.migratemusic.mvp.view.ChooseMusicServiceView

class ChooseMusicServiceActivity : AppCompatActivity(), ChooseMusicServiceView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_music_service)
        ChooseMusicServicePresenter.view = this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ChooseMusicServicePresenter.onLoginActivityResult(requestCode, resultCode, data)
    }

    override fun getActivity(): Activity = this

    fun onSpotifyLoginClick(view: View) {
        ChooseMusicServicePresenter.onSpotifyLoginClick()
    }

    fun onDeezerLoginClick(view: View) {
        ChooseMusicServicePresenter.onDeezerLoginClick()
    }

    fun onYoutubeMusicLoginClick(view: View) {
        ChooseMusicServicePresenter.onYoutubeMusicLoginClick()
    }

}