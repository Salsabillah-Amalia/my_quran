package com.example.my_quran.di

import com.example.my_quran.data.repository.SurahRepository
import com.example.my_quran.data.repository.SurahRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSurahRepository(
        impl: SurahRepositoryImpl
    ): SurahRepository
}
