package com.hana.fieldmate.ui.navigation

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigateModule {
    @Singleton
    @Provides
    fun providesNavigator() =
        ComposeCustomNavigator()
}