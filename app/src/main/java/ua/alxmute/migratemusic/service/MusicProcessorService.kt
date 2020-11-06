package ua.alxmute.migratemusic.service

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.LocalTrackDto
import ua.alxmute.migratemusic.service.factory.AddTrackChainFactory
import ua.alxmute.migratemusic.service.factory.MusicServiceStrategyFactory
import ua.alxmute.migratemusic.service.listener.TrackProcessingListener

object MusicProcessorService {

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Default)

    fun addTracks(
        tracksToProcess: List<LocalTrackDto>,
        processedTracks: MutableList<LocalTrackDto>,
        listener: TrackProcessingListener
    ) {

        val musicServiceStrategy = MusicServiceStrategyFactory[ContextHolder.musicServiceName]

        tracksToProcess.forEach { track ->
            coroutineScope.launch {
                val result: Boolean = AddTrackChainFactory.handle(track, musicServiceStrategy)

                // TODO: discover what to do with not migrated tracks
                if (result) {
                    Log.d("success", "${track.author} ${track.title}")
                } else {
                    Log.d("failure", "${track.fileName} (${track.author} ${track.title})")
                }

                processedTracks.add(track)

                listener.onTrackProcessed(processedTracks.size, tracksToProcess.size)
            }
        }
    }

}