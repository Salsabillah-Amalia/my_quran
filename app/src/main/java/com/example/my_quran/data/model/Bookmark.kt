package com.example.my_quran.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey
    val ayahId: String,
    val ayahNumber: Int,
    val arabicText: String,
    val translation: String,
    val numberInSurah: Int,
    val juz: Int,
    val manzil: Int,
    val page: Int,
    val surahNumber: Int,
    val timestamp: Long
)