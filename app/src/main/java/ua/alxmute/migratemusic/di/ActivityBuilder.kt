package ua.alxmute.migratemusic.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ua.alxmute.migratemusic.activity.ChooseDirectoryActivity
import ua.alxmute.migratemusic.activity.ChooseMusicServiceActivity
import ua.alxmute.migratemusic.activity.FileProcessingActivity
import ua.alxmute.migratemusic.di.module.FileProcessingActivityModule

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindChooseDirectoryActivity(): ChooseDirectoryActivity

    @ContributesAndroidInjector
    abstract fun bindChooseMusicServiceActivity(): ChooseMusicServiceActivity

    @ContributesAndroidInjector(modules = [FileProcessingActivityModule::class])
    abstract fun bindFileProcessingActivity(): FileProcessingActivity

}