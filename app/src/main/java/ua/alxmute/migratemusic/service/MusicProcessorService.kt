package ua.alxmute.migratemusic.service

import android.util.Log
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.LocalTrackDto
import ua.alxmute.migratemusic.service.factory.AddTrackChainFactory
import ua.alxmute.migratemusic.service.factory.MusicServiceStrategyFactory
import ua.alxmute.migratemusic.service.listener.TrackProcessingListener

object MusicProcessorService {

    fun addTracks(
        tracksToProcess: List<LocalTrackDto>,
        processedTracks: MutableList<LocalTrackDto>,
        listener: TrackProcessingListener
    ) {

        val musicServiceStrategy = MusicServiceStrategyFactory[ContextHolder.musicServiceName]

        tracksToProcess.forEach { localTrackDto ->
            val result: Boolean = AddTrackChainFactory.handle(localTrackDto, musicServiceStrategy)
            if (result) {
                processedTracks.add(localTrackDto)
                Log.d("success", "${localTrackDto.author} ${localTrackDto.title}")
            } else {
                // TODO: add failed tracks to separate list
//                processedTracks.add(localTrackDto)
                Log.d(
                    "failure",
                    "${localTrackDto.fileName} (${localTrackDto.author} ${localTrackDto.title})"
                )
            }

            listener.onTrackProcessed(processedTracks.size, tracksToProcess.size)

        }
    }

}