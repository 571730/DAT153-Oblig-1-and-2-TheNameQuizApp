package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Entity.ScoreEntity
import com.example.myapplication.Repositories.ScoreRepository
import kotlinx.coroutines.launch

class ScoreViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ScoreRepository
    val allScores: LiveData<List<ScoreEntity>>

    init {
        val scoreDao = AppDatabase.getDatabase(application, viewModelScope).scoreDao()
        repository = ScoreRepository(scoreDao)
        allScores = repository.allScores
    }

    fun insert(score: ScoreEntity) = viewModelScope.launch {
        repository.insert(score)
    }
}