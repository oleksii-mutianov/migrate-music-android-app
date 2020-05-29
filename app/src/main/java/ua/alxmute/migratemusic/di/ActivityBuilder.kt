package ua.alxmute.migratemusic.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ua.alxmute.migratemusic.activity.ChooseDirectoryActivity
import ua.alxmute.migratemusic.activity.ChooseMusicServiceActivity
import ua.alxmute.migratemusic.activity.FileProcessingActivity


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindChooseDirectoryActivity(): ChooseDirectoryActivity

    @ContributesAndroidInjector
    abstract fun bindChooseMusicServiceActivity(): ChooseMusicServiceActivity

    @ContributesAndroidInjector
    abstract fun bindFileProcessingActivity(): FileProcessingActivity

}