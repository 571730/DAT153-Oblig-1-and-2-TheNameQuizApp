package com.example.myapplication

import com.example.myapplication.Entity.PersonEntity
import kotlin.math.roundToInt

/**
 * class is used to keep track of the quiz and all its states
 * @param data is the list of people from the database
 * @property score keeps track of the users correct answers
 * @property attempts total amount of attempts
 * @property done is the quiz done? true \ false
 * @property index points to the current position in the list of people
 * @property people a shuffled copy of the data
 */
class Quiz (data: ArrayList<PersonEntity>){
    var score: Int
    var attempts: Int
    var done: Boolean
    var index: Int
    var people: ArrayList<PersonEntity>

    /**
     * called when the user submits an answer
     * @param ans the name the user guessed
     * @return was the answer correct?
     */
    fun answer(ans: String): AnswerFeedback{
        attempts++
        val answerFeedback = AnswerFeedback(false, people[index].name)
        if (ans.equals(people[index].name, true)) {
            score++
            answerFeedback.isCorrect = true
        }
        index++
        done = isDone()
        return  answerFeedback
    }

    /**
     * gets the next person in the quiz
     * @return the next person object
     */
    fun pickPerson(): PersonEntity {
        val person = people[index]
        return person
    }

    /**
     * check if the quiz is done
     * @return is it done?
     */
    private fun isDone(): Boolean {
        return attempts == people.size
    }

    init {
        score = 0
        attempts = 0
        index = 0
        done = false
        data.shuffle()
        people = data
    }

    fun calculateFinalScore(): Int {
        val calculatedScore = (attempts * 100).toDouble() * (score.toFloat()/attempts.toFloat())
        return calculatedScore.roundToInt()
    }
}