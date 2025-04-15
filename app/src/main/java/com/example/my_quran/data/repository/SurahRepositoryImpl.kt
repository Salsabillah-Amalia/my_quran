package com.example.my_quran.data.repository

import com.example.my_quran.data.model.Resource
import com.example.my_quran.data.model.Surah
import com.example.my_quran.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SurahRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SurahRepository {

    override fun getSurahs(): Flow<Resource<List<Surah>>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getSurahs()
            emit(Resource.Success(response.data))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected error"))
        }
    }
}
