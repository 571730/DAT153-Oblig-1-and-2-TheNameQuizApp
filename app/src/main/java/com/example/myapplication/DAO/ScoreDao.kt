package com.example.myapplication.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.Entity.ScoreEntity

@Dao
interface ScoreDao {
    @Query("SELECT * FROM scoreentity")
    fun getAll(): LiveData<List<ScoreEntity>>

    @Insert
    suspend fun insert(person: ScoreEntity)
}