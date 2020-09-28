package ua.alxmute.migratemusic.service.listener

interface TrackProcessingListener {
    fun onTrackProcessed(processedSize: Int, totalSize: Int)
}