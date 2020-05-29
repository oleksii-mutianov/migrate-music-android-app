package ua.alxmute.migratemusic.config

import dagger.Component
import ua.alxmute.migratemusic.ChooseDirectoryActivity
import ua.alxmute.migratemusic.ChooseMusicServiceActivity
import ua.alxmute.migratemusic.FileProcessingActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        ContextHolderModule::class,
        SpotifyServiceModule::class
    ]
)
interface AppComponent {

    fun inject(chooseMusicServiceActivity: ChooseMusicServiceActivity)

    fun inject(chooseDirectoryActivity: ChooseDirectoryActivity)

    fun inject(fileProcessingActivity: FileProcessingActivity)

}