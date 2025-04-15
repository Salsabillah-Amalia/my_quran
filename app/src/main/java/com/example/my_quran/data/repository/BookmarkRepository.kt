package com.example.my_quran.data.repository

import com.example.my_quran.data.local.BookmarkDao
import com.example.my_quran.data.model.Ayah
import com.example.my_quran.data.model.Bookmark
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarkRepository @Inject constructor(
    private val bookmarkDao: BookmarkDao
) {

    fun getAllBookmarks(): Flow<List<Ayah>> {
        return bookmarkDao.getAllBookmarks().map { bookmarks ->
            bookmarks.map { bookmark ->
                Ayah(
                    id = bookmark.ayahId,
                    number = bookmark.ayahNumber,
                    text = bookmark.arabicText,
                    numberInSurah = bookmark.numberInSurah,
                    juz = bookmark.juz,
                    manzil = bookmark.manzil,
                    page = bookmark.page,
                    surahNumber = bookmark.surahNumber,
                    translation = bookmark.translation
                )
            }
        }
    }

    suspend fun toggleBookmark(ayah: Ayah) {
        val existingBookmark = bookmarkDao.getBookmarkByAyahId(ayah.id)

        if (existingBookmark != null) {
            bookmarkDao.deleteBookmark(existingBookmark)
        } else {
            val bookmark = Bookmark(
                ayahId = ayah.id,
                ayahNumber = ayah.number,
                arabicText = ayah.text,
                translation = ayah.translation,
                numberInSurah = ayah.numberInSurah,
                juz = ayah.juz,
                manzil = ayah.manzil,
                page = ayah.page,
                surahNumber = ayah.surahNumber,
                timestamp = System.currentTimeMillis()
            )
            bookmarkDao.insertBookmark(bookmark)
        }
    }

    suspend fun isBookmarked(ayahId: String): Boolean {
        return bookmarkDao.getBookmarkByAyahId(ayahId) != null
    }
}