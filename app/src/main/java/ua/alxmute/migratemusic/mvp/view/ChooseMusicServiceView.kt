package ua.alxmute.migratemusic.mvp.view

import android.app.Activity

interface ChooseMusicServiceView {

    fun getActivity(): Activity

    fun getString(stringId: Int): String

}