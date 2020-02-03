package com.example.myapplication.Entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = -1,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "picture") val picture: Bitmap?
)