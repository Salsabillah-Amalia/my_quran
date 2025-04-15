package com.example.my_quran.data.repository

import com.example.my_quran.data.local.QuranDatabase
import com.example.my_quran.data.model.Ayah
import com.example.my_quran.data.model.Resource
import com.example.my_quran.data.model.Surah
import com.example.my_quran.data.remote.QuranApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.flow.single

class QuranRepositoryImpl @Inject constructor(
    private val apiService: QuranApiService,
    private val database: QuranDatabase
) : QuranRepository {

    override fun getSurahs(): Flow<Resource<List<Surah>>> {
        return database.surahDao().getAllSurahs().map { surahs ->
            if (surahs.isNotEmpty()) {
                Resource.Success(surahs)
            } else {
                Resource.Loading
            }
        }
    }

    override fun getAyahsBySurah(surahNumber: Int): Flow<Resource<List<Ayah>>> {
        return database.ayahDao().getAyahsBySurah(surahNumber).map { ayahs ->
            if (ayahs.isNotEmpty()) {
                Resource.Success(ayahs)
            } else {
                Resource.Loading
            }
        }
    }

    override suspend fun refreshSurahs() {
        try {
            val response = apiService.getSurahs()
            if (response.code == 200) {
                database.surahDao().deleteAll()
                database.surahDao().insertAll(response.data)
            }
        } catch (e: Exception) {
            // Handle error
        }
    }

    override suspend fun refreshAyahs(surahNumber: Int) {
        try {
            val response = apiService.getSurah(surahNumber)
            val translationResponse = apiService.getSurahTranslation(surahNumber)

            if (response.code == 200 && translationResponse.code == 200) {
                database.ayahDao().deleteAyahsBySurah(surahNumber)

                // Combine Arabic text with translations
                val ayahsWithTranslation = response.data.ayahs.mapIndexed { index, ayah ->
                    ayah.copy(
                        id = "${ayah.surahNumber}_${ayah.numberInSurah}",
                        translation = translationResponse.data.ayahs.getOrNull(index)?.text ?: ""
                    )
                }

                database.ayahDao().insertAll(ayahsWithTranslation)
            }
        } catch (e: Exception) {
            // Handle error
        }
    }

    override suspend fun searchQuran(keyword: String): Resource<List<Ayah>> = flow {
        try {
            val response = apiService.searchQuran(keyword)
            if (response.code == 200) {
                emit(Resource.Success(response.data.ayahs))
            } else {
                emit(Resource.Error("Error: ${response.status}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }.map { it }.single()

    override suspend fun getRandomAyah(): Resource<Ayah> {
        return try {
            // Pick a random surah between 1 and 114
            val randomSurah = Random.nextInt(1, 115)
            val surahData = apiService.getSurah(randomSurah)
            val ayahsCount = surahData.data.numberOfAyahs
            val randomAyahNumber = Random.nextInt(1, ayahsCount + 1)

            val randomAyah = surahData.data.ayahs.find { it.numberInSurah == randomAyahNumber }

            // Get the translation
            val translationResponse = apiService.getSurahTranslation(randomSurah)
            val translationAyah = translationResponse.data.ayahs.find { it.numberInSurah == randomAyahNumber }

            if (randomAyah != null && translationAyah != null) {
                Resource.Success(
                    randomAyah.copy(
                        id = "${randomAyah.surahNumber}_${randomAyah.numberInSurah}",
                        translation = translationAyah.text
                    )
                )
            } else {
                Resource.Error("Ayah not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }
}
