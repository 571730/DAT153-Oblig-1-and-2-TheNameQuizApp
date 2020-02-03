package com.example.myapplication.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.Entity.PersonEntity

@Dao
interface PersonDAO {
    @Query("SELECT * FROM personentity")
    fun getAll(): LiveData<List<PersonEntity>>

    @Query("SELECT * FROM personentity WHERE uid IN (:personIds)")
    fun loadAllByIds(personIds: IntArray): List<PersonEntity>

    @Insert
    fun insertAll(vararg persons: PersonEntity)

    @Insert
    fun insert(person: PersonEntity)

    @Delete
    fun delete(person: PersonEntity)
}
