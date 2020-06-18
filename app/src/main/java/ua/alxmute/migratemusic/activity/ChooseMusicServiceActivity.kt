package ua.alxmute.migratemusic.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.activity.presenter.ChooseMusicServicePresenter
import ua.alxmute.migratemusic.activity.view.ChooseMusicServiceView
import javax.inject.Inject

class ChooseMusicServiceActivity : DaggerAppCompatActivity(), ChooseMusicServiceView {

    @Inject
    lateinit var chooseMusicServicePresenter: ChooseMusicServicePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_music_service)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        chooseMusicServicePresenter.onSpotifyActivityResult(requestCode, resultCode, data)
    }

    override fun getActivity(): Activity = this

    fun onSpotifyLoginClick(view: View) {
        chooseMusicServicePresenter.onSpotifyLoginClick()
    }

    fun onDeezerLoginClick(view: View) {
        chooseMusicServicePresenter.onDeezerLoginClick()
    }

    fun onYoutubeMusicLoginClick(view: View) {
        chooseMusicServicePresenter.onYoutubeMusicLoginClick()
    }

}
