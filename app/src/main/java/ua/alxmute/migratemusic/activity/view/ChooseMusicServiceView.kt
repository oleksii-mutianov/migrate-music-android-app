package ua.alxmute.migratemusic.activity.view

import android.app.Activity

interface ChooseMusicServiceView {

    fun getActivity(): Activity

    fun getString(stringId: Int): String

}