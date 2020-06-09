package ua.alxmute.migratemusic.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import ua.alxmute.migratemusic.MusicMigrationApp
import ua.alxmute.migratemusic.di.module.*
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuilder::class,
        AuthClientModule::class,
        AddTrackChainModule::class,
        DirectoryProcessorModule::class,
        MusicServiceStrategyModule::class,
        NetworkModule::class,
        ContextHolderModule::class
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application?): Builder?
        fun build(): AppComponent?
    }

    fun inject(app: MusicMigrationApp)

    override fun inject(instance: DaggerApplication)

}