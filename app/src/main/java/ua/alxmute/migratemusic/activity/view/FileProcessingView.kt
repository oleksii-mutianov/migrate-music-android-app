package ua.alxmute.migratemusic.activity.view

import android.content.res.Resources
import ua.alxmute.migratemusic.data.ProcessedTrackDto

interface FileProcessingView {

    fun refreshList()

    fun setTrackCounter(text: String)

    fun getResources(): Resources

    fun getListForProcessedTracks(): MutableList<ProcessedTrackDto>

    fun setFailureTrackCounter(counter: String)

}