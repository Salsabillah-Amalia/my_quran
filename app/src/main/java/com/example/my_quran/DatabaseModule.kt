package com.example.my_quran.di

import com.example.my_quran.data.local.BookmarkDao
import com.example.my_quran.data.local.QuranDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideBookmarkDao(db: QuranDatabase): BookmarkDao {
        return db.bookmarkDao()
    }

}
