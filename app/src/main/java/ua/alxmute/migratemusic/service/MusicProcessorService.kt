package ua.alxmute.migratemusic.service

import ua.alxmute.migratemusic.chain.AddTrackChain
import ua.alxmute.migratemusic.data.ContextHolder
import ua.alxmute.migratemusic.data.LocalTrackDto
import ua.alxmute.migratemusic.data.MusicServiceName
import ua.alxmute.migratemusic.data.ProcessedTrackDto
import ua.alxmute.migratemusic.strategy.MusicServiceStrategy

class MusicProcessorService(
    private val musicServiceStrategies: Map<MusicServiceName, MusicServiceStrategy>,
    private val contextHolder: ContextHolder,
    private val addTrackChain: AddTrackChain
) {
    var failureCounter = 0
    fun addTracks(
        tracksToProcess: List<LocalTrackDto>,
        processedTracks: MutableList<ProcessedTrackDto>,
        listener: TrackProcessingListener
    ) {

        val musicServiceStrategy = musicServiceStrategies.getValue(contextHolder.musicServiceName)

        tracksToProcess.forEach { localTrackDto ->
            val result: Boolean = addTrackChain.handle(localTrackDto, musicServiceStrategy)
            processedTracks.add(
                ProcessedTrackDto(
                    localTrackDto.title,
                    localTrackDto.author,
                    localTrackDto.fileName,
                    !result
                )
            )

            if (!result) {
                failureCounter++
            }

            listener.onTrackProcessed(processedTracks.size, tracksToProcess.size, failureCounter)

        }
        failureCounter = 0
    }

}