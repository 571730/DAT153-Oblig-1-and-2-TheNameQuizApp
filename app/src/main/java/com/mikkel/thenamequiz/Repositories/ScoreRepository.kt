package com.example.myapplication.Repositories

import androidx.lifecycle.LiveData
import com.example.myapplication.DAO.ScoreDao
import com.example.myapplication.Entity.ScoreEntity

class ScoreRepository (private val scoreDao: ScoreDao) {
    val allScores : LiveData<List<ScoreEntity>> = scoreDao.getAll()

    suspend fun insert(score: ScoreEntity){
        scoreDao.insert(score)
    }
}
