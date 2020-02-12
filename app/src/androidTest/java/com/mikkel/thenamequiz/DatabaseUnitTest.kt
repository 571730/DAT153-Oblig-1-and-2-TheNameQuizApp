package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.Entity.PersonEntity
import com.example.myapplication.Repositories.PersonRepository
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DatabaseUnitTest {
    private lateinit var appDatabase: AppDatabase
    private lateinit var repo: PersonRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        repo = PersonRepository(appDatabase.personDao())
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testAddPerson() {
        val myScope = GlobalScope
        runBlocking {
            myScope.launch {
                repo.insert(PersonEntity(0, "Donald", "filePath"))
                repo.insert(PersonEntity(2, "Barrack", "filePath"))
            }
        }.invokeOnCompletion {
            assertTrue(repo.allPeople.value!!.size == 2)
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun testAddRemovePerson() {
        val myScope = GlobalScope
        val personOne = PersonEntity(0, "Donald", "filePath")
        val personTwo = PersonEntity(2, "Barrack", "filePath")
        runBlocking {
            myScope.launch {
                repo.insert(personOne)
                repo.insert(personTwo)
            }
        }.invokeOnCompletion {
            assertTrue(repo.allPeople.value!!.size == 2)
        }
        runBlocking {
            myScope.launch {
                repo.remove(personOne)
            }
        }.invokeOnCompletion {
            assertTrue(repo.allPeople.value!!.size == 1)
        }
    }

}