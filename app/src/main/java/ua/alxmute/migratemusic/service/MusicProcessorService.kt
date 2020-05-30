package ua.alxmute.migratemusic.service

import ua.alxmute.migratemusic.data.LocalTrackDto
import javax.inject.Inject

class MusicProcessorService
@Inject constructor(
    private val tracksService: AddTracksService
) {

    fun addTracks(tracks: List<LocalTrackDto>) {

        val successfulList = ArrayList<LocalTrackDto>()
        val unsuccessfulList = ArrayList<LocalTrackDto>()

        tracks.forEach { localTrackDto ->
            val result: Boolean = tracksService.addTrack(localTrackDto)
            if (result) {
                successfulList.add(localTrackDto)
            } else {
                unsuccessfulList.add(localTrackDto)
            }
        }
    }

}