package com.example.my_quran.data.network

import com.example.my_quran.data.model.QuranResponse
import retrofit2.http.GET

interface ApiService {
    @GET("surah")
    suspend fun getSurahs(): QuranResponse
}
