package ua.alxmute.migratemusic.di

import dagger.MapKey
import ua.alxmute.migratemusic.data.MusicServiceName

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class MusicServiceNameKey(val value: MusicServiceName)