package com.example.myapplication.Repositories

import androidx.lifecycle.LiveData
import com.example.myapplication.DAO.PersonDAO
import com.example.myapplication.Entity.PersonEntity

class PersonRepository (private val personDAO: PersonDAO) {
    val allPeople : LiveData<List<PersonEntity>> = personDAO.getAll()

    suspend fun insert(person: PersonEntity){
        personDAO.insert(person)
    }

    suspend fun remove(person: PersonEntity) {
        personDAO.delete(person)
    }
}