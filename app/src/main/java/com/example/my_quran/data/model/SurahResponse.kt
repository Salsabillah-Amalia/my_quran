package com.example.my_quran.data.model

data class SurahResponse(
    val code: Int,
    val status: String,
    val data: List<Surah>
)
