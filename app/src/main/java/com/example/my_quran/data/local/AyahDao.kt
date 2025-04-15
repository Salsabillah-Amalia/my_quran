package com.example.my_quran.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.my_quran.data.model.Ayah
import kotlinx.coroutines.flow.Flow

@Dao
interface AyahDao {
    @Query("SELECT * FROM ayahs WHERE surahNumber = :surahNumber ORDER BY numberInSurah")
    fun getAyahsBySurah(surahNumber: Int): Flow<List<Ayah>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ayahs: List<Ayah>)

    @Query("DELETE FROM ayahs WHERE surahNumber = :surahNumber")
    suspend fun deleteAyahsBySurah(surahNumber: Int)
}
