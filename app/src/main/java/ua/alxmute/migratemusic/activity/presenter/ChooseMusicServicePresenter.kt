package ua.alxmute.migratemusic.activity.presenter

import android.content.Intent

interface ChooseMusicServicePresenter {

    fun onSpotifyLoginClick()

    fun onSpotifyActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    fun onDeezerLoginClick()

}