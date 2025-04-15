package com.example.my_quran.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.my_quran.data.model.Ayah
import com.example.my_quran.data.model.Surah
import com.example.my_quran.data.model.Bookmark

@Database(
    entities = [Surah::class, Ayah::class, Bookmark::class],
    version = 1,
    exportSchema = false
)
abstract class QuranDatabase : RoomDatabase() {
    abstract fun surahDao(): SurahDao
    abstract fun ayahDao(): AyahDao
    abstract fun bookmarkDao(): BookmarkDao
}
