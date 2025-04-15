package com.example.my_quran.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ayahs")
data class Ayah(
    @PrimaryKey
    val id: String,

    @SerializedName("number")
    val number: Int,

    @SerializedName("text")
    val text: String,

    @SerializedName("numberInSurah")
    val numberInSurah: Int,

    @SerializedName("juz")
    val juz: Int,

    @SerializedName("manzil")
    val manzil: Int,

    @SerializedName("page")
    val page: Int,

    @SerializedName("surah")
    val surahNumber: Int,

    var translation: String = ""
)

data class AyahResponse(
    @SerializedName("code")
    val code: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("data")
    val data: AyahData
)

data class AyahData(
    @SerializedName("number")
    val number: Int,

    @SerializedName("ayahs")
    val ayahs: List<Ayah>,

    @SerializedName("name")
    val name: String,

    @SerializedName("englishName")
    val englishName: String,

    @SerializedName("englishNameTranslation")
    val englishNameTranslation: String,

    @SerializedName("revelationType")
    val revelationType: String,

    @SerializedName("numberOfAyahs")
    val numberOfAyahs: Int
)