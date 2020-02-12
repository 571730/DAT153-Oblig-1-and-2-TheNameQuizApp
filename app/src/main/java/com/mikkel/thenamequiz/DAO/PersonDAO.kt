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

    @Query("SELECT * FROM personentity WHERE id IN (:personIds)")
    fun loadAllByIds(personIds: IntArray): List<PersonEntity>

    @Insert
    fun insertAll(vararg persons: PersonEntity)

    @Insert
    suspend fun insert(person: PersonEntity)

    @Delete
    suspend fun delete(person: PersonEntity)

    @Query("SELECT * FROM personentity")
    suspend fun getPeopleList(): List<PersonEntity>

}
