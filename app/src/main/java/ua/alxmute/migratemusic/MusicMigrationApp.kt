package ua.alxmute.migratemusic

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ua.alxmute.migratemusic.di.DaggerAppComponent

class MusicMigrationApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this)?.build()
        appComponent?.inject(this)
        return appComponent!!
    }
}