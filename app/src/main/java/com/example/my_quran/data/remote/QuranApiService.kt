package com.example.my_quran.data.remote

import com.example.my_quran.data.model.AyahResponse
import com.example.my_quran.data.model.QuranResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranApiService {
    @GET("surah")
    suspend fun getSurahs(): QuranResponse

    @GET("surah/{surahNumber}")
    suspend fun getSurah(@Path("surahNumber") surahNumber: Int): AyahResponse

    @GET("surah/{surahNumber}/en.asad")
    suspend fun getSurahTranslation(@Path("surahNumber") surahNumber: Int): AyahResponse

    @GET("juz/{juzNumber}")
    suspend fun getJuz(@Path("juzNumber") juzNumber: Int): AyahResponse

    @GET("search/{keyword}")
    suspend fun searchQuran(@Path("keyword") keyword: String): AyahResponse
}