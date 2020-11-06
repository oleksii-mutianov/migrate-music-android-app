package ua.alxmute.migratemusic.mvp.view

import android.app.Activity
import android.content.res.Resources
import ua.alxmute.migratemusic.data.LocalTrackDto

interface FileProcessingView {

    val processedTracks: MutableList<LocalTrackDto>

    fun refreshList()

    fun setTrackCounter(text: String)

    fun getResources(): Resources

    fun getActivity(): Activity

}