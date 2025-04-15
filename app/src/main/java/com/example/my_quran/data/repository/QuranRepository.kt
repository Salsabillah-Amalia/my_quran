package com.example.my_quran.data.repository

import com.example.my_quran.data.model.Ayah
import com.example.my_quran.data.model.Resource
import com.example.my_quran.data.model.Surah
import kotlinx.coroutines.flow.Flow

interface QuranRepository {
    fun getSurahs(): Flow<Resource<List<Surah>>>
    fun getAyahsBySurah(surahNumber: Int): Flow<Resource<List<Ayah>>>
    suspend fun refreshSurahs()
    suspend fun refreshAyahs(surahNumber: Int)
    suspend fun searchQuran(keyword: String): Resource<List<Ayah>>
    suspend fun getRandomAyah(): Resource<Ayah>
}
