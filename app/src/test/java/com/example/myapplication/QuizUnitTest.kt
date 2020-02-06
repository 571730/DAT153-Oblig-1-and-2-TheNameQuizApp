package com.example.myapplication

import com.example.myapplication.Entity.PersonEntity
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test

class QuizUnitTest {
    private var mockPeople: ArrayList<PersonEntity> = ArrayList()
    private var quiz: Quiz

    init {
        mockPeople.add(PersonEntity(0, "Donald", "Filepath"))
        mockPeople.add(PersonEntity(0, "Barrack", "Filepath"))
        quiz = Quiz(mockPeople)
    }

    @Test
    fun checkCorrectAnswer(){
        val shouldBeCorrectName: String = quiz.pickPerson().name
        assertTrue(quiz.score == 0)
        val feedback = quiz.answer(shouldBeCorrectName)
        assertTrue(feedback.isCorrect)
        assertTrue(quiz.score == 1)
        assertTrue(quiz.attempts == 1)
    }

    @Test
    fun checkWrongAnswer(){
        val shouldBeWrongName = "This is not a name"
        assertTrue(quiz.score == 0)
        val feedback = quiz.answer(shouldBeWrongName)
        assertFalse(feedback.isCorrect)
        assertTrue(quiz.score == 0)
        assertTrue(quiz.attempts == 1)
    }

}