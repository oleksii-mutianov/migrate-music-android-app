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

    fun addTracks(tracks: List<LocalTrackDto>) {

        val successfulList = ArrayList<LocalTrackDto>()
        val unsuccessfulList = ArrayList<LocalTrackDto>()

        val musicServiceStrategy = musicServiceStrategies.getValue(contextHolder.musicServiceName)

        tracks.forEach { localTrackDto ->
            val result: Boolean = addTrackChain.handle(localTrackDto, musicServiceStrategy)
            if (result) {
                successfulList.add(localTrackDto)
                Log.d("success", "${localTrackDto.author} ${localTrackDto.title}")
            } else {
                unsuccessfulList.add(localTrackDto)
                Log.d("failure", "${localTrackDto.fileName} (${localTrackDto.author} ${localTrackDto.title})")
            }
        }
    }

}