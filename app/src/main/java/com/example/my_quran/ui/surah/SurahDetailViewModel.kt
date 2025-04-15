package com.example.my_quran.ui.surah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_quran.data.model.Ayah
import com.example.my_quran.data.model.Resource
import com.example.my_quran.data.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurahDetailViewModel @Inject constructor(
    private val repository: QuranRepository
) : ViewModel() {

    private val _ayahs = MutableStateFlow<Resource<List<Ayah>>>(Resource.Loading)
    val ayahs: StateFlow<Resource<List<Ayah>>> = _ayahs

    fun loadSurahDetail(surahNumber: Int) {
        viewModelScope.launch {
            repository.getAyahsBySurah(surahNumber).collectLatest { result ->
                _ayahs.value = result

                // If no cached data available, fetch from API
                if (result is Resource.Loading) {
                    refreshAyahs(surahNumber)
                }
            }
        }
    }

    fun refreshAyahs(surahNumber: Int) {
        viewModelScope.launch {
            repository.refreshAyahs(surahNumber)
        }
    }
}