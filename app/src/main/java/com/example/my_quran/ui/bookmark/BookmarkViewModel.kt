package com.example.my_quran.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_quran.data.model.Ayah
import com.example.my_quran.data.model.Resource
import com.example.my_quran.data.repository.BookmarkRepository
import com.example.my_quran.data.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val quranRepository: QuranRepository
) : ViewModel() {

    val bookmarks: StateFlow<List<Ayah>> = bookmarkRepository.getAllBookmarks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _randomAyah = MutableStateFlow<Ayah?>(null)
    val randomAyah: StateFlow<Ayah?> = _randomAyah

    init {
        getRandomAyah()
    }

    fun getRandomAyah() {
        viewModelScope.launch {
            when (val result = quranRepository.getRandomAyah()) {
                is Resource.Success -> _randomAyah.value = result.data
                else -> {} // Handle error
            }
        }
    }

    fun toggleBookmark(ayah: Ayah) {
        viewModelScope.launch {
            bookmarkRepository.toggleBookmark(ayah)
        }
    }
}