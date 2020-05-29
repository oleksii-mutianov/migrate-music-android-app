package ua.alxmute.migratemusic.config

import dagger.Component
import ua.alxmute.migratemusic.SpotifyLoginActivity
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
    fun inject(spotifyLoginActivity: SpotifyLoginActivity)
}