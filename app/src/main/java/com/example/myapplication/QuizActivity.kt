package com.example.myapplication

import android.animation.Animator
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {
    lateinit var quiz: Quiz
    lateinit var data: ArrayList<Person>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        data = (application as AppSingleton).data
        quiz = Quiz(data)
        inputAnswer.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (!quiz.done){
                    submitAnswer(inputAnswer)
                }
                return@OnKeyListener true
            }
            false
        })
        runRound()
    }

    private fun runRound(){
        val person = quiz.pickPerson()
        imageViewGuess.setImageBitmap(person.image)
    }

    fun restart(view: View){
        quiz = Quiz(data)
        buttonRestart.visibility = View.INVISIBLE
        btnSubmit.visibility = View.VISIBLE
        textPersons.text = "0"
        textScore.text = "0"
        runRound()
    }

    fun submitAnswer(view: View){
        val isCorrect = quiz.answer(inputAnswer.text.toString())
        textPersons.text = quiz.attempts.toString()
        textScore.text = quiz.score.toString()
        displayFeedback(isCorrect)
        inputAnswer.setText("")
        if (!quiz.done){
            runRound()
        } else {
            Toast.makeText(this, "Well played!", Toast.LENGTH_SHORT).show()
            btnSubmit.visibility = View.INVISIBLE
            buttonRestart.visibility = View.VISIBLE
            inputAnswer.hideKeyboard()
        }
    }

    private fun displayFeedback(isCorrect: Boolean){
       if(isCorrect) {
           textFeedback.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_24dp, 0, 0, 0)
       } else {
           textFeedback.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_24dp, 0, 0, 0)
       }
        textFeedback.visibility = View.VISIBLE
        textFeedback.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.left_right))
        textFeedback.visibility = View.INVISIBLE
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
