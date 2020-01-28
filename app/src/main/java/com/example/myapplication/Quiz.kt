package com.example.myapplication

class Quiz (data: ArrayList<Person>){
    var score: Int
    var attempts: Int
    var done: Boolean
    var index: Int
    var people: ArrayList<Person>

    fun answer(ans: String): Boolean{
        attempts++
        var correct = false
        if (ans.equals(people[index].name, true)) {
            score++
            correct = true
        }
        index++
        done = isDone()
        return  correct
    }

    fun pickPerson(): Person {
        val person = people[index]
        return person
    }

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
}