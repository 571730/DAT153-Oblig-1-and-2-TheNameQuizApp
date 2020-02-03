package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Entity.PersonEntity
import com.example.myapplication.Repositories.PersonRepository
import kotlinx.coroutines.launch

class PersonViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: PersonRepository
    // LiveData gives us updated words when they change.
    val allPeople: LiveData<List<PersonEntity>>

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
        val personDao = AppDatabase.getDatabase(application, viewModelScope).personDao()
        repository = PersonRepository(personDao)
        allPeople = repository.allPeople
    }

    /**
     * The implementation of insert() in the database is completely hidden from the UI.
     * Room ensures that you're not doing any long running operations on
     * the main thread, blocking the UI, so we don't need to handle changing Dispatchers.
     * ViewModels have a coroutine scope based on their lifecycle called
     * viewModelScope which we can use here.
     */
    fun insert(person: PersonEntity) = viewModelScope.launch {
        repository.insert(person)
    }
}

