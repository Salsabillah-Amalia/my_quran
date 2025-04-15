package com.example.my_quran.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "surahs")
data class Surah(
    @PrimaryKey
    @SerializedName("number")
    val number: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("englishName")
    val englishName: String,

    @SerializedName("englishNameTranslation")
    val englishNameTranslation: String,

    @SerializedName("numberOfAyahs")
    val numberOfAyahs: Int,

    @SerializedName("revelationType")
    val revelationType: String
)

data class QuranResponse(
    @SerializedName("code")
    val code: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("data")
    val data: List<Surah>
)