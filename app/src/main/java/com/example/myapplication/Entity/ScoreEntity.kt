package com.example.myapplication.Entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true) @NonNull val id: Int = 0,
    @ColumnInfo(name = "name") @NonNull val name: String,
    @ColumnInfo(name = "score") @NonNull val score: Int
)
