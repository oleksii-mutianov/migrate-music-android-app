package ua.alxmute.migratemusic.service

import android.app.Activity
import android.util.Log
import android.widget.TextView
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.adapter.TrackRecyclerViewAdapter
import ua.alxmute.migratemusic.chain.AddTrackChain
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.LocalTrackDto
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.strategy.MusicServiceStrategy

class MusicProcessorService(
    private val musicServiceStrategies: Map<MusicServiceName, MusicServiceStrategy>,
    private val contextHolder: ContextHolder,
    private val addTrackChain: AddTrackChain
) {

    fun addTracks(
        tracksToProcess: List<LocalTrackDto>,
        processedTracks: MutableList<LocalTrackDto>,
        adapter: TrackRecyclerViewAdapter
    ) {

        val musicServiceStrategy = musicServiceStrategies.getValue(contextHolder.musicServiceName)

        tracksToProcess.forEach { localTrackDto ->
            val result: Boolean = addTrackChain.handle(localTrackDto, musicServiceStrategy)
            if (result) {
                processedTracks.add(localTrackDto)
                Log.d("success", "${localTrackDto.author} ${localTrackDto.title}")
            } else {
                // TODO: add failed tracks to separate list
                processedTracks.add(localTrackDto)
                Log.d("failure", "${localTrackDto.fileName} (${localTrackDto.author} ${localTrackDto.title})")
            }

            // TODO: implement MVP to get rid of drawing logic in service...
            (adapter.context as Activity).runOnUiThread {
                adapter.notifyDataSetChanged()
                adapter.context.findViewById<TextView>(R.id.trackCounter).text =
                    adapter.context.resources.getString(R.string.processed_tracks, processedTracks.size, tracksToProcess.size)
            }

        }
    }

}