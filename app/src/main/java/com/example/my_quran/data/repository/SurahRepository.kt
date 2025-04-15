package com.example.my_quran.data.repository

import com.example.my_quran.data.model.Resource
import com.example.my_quran.data.model.Surah
import kotlinx.coroutines.flow.Flow

interface SurahRepository {
    fun getSurahs(): Flow<Resource<List<Surah>>>
}
