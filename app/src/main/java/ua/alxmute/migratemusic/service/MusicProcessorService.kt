package ua.alxmute.migratemusic.service

import android.util.Log
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
        listener: TrackProcessingListener
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

            listener.onTrackProcessed(processedTracks.size, tracksToProcess.size)

        }
    }

}