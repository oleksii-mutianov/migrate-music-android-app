package ua.alxmute.migratemusic.service

interface TrackProcessingListener {
    fun onTrackProcessed(processedSize: Int, totalSize: Int, failureCounter: Int)
}