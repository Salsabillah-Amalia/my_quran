package com.example.my_quran.ui.surah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_quran.data.model.Resource
import com.example.my_quran.data.model.Surah
import com.example.my_quran.data.repository.SurahRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurahViewModel @Inject constructor(
    private val repository: SurahRepository
) : ViewModel() {

    private val _surahs = MutableStateFlow<Resource<List<Surah>>>(Resource.Loading)
    val surahs: StateFlow<Resource<List<Surah>>> = _surahs

    init {
        getSurahs()
    }

    private fun getSurahs() {
        viewModelScope.launch {
            repository.getSurahs().collect { result ->
                _surahs.value = result
            }
        }
    }

    fun refreshSurahs() {
        getSurahs()
    }
}
