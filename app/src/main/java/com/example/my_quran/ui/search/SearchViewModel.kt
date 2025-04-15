package com.example.my_quran.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_quran.data.model.Ayah
import com.example.my_quran.data.model.Resource
import com.example.my_quran.data.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: QuranRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<Resource<List<Ayah>>>(Resource.Success(emptyList()))
    val searchResults: StateFlow<Resource<List<Ayah>>> = _searchResults

    fun searchQuran(query: String) {
        if (query.length < 3) {
            _searchResults.value = Resource.Success(emptyList())
            return
        }

        _searchResults.value = Resource.Loading

        viewModelScope.launch {
            _searchResults.value = repository.searchQuran(query)
        }
    }
}