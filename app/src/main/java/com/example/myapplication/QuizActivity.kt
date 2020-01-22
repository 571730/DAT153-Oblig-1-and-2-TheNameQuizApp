package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {
    lateinit var quiz: Quiz
    lateinit var data: ArrayList<Person>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        val editText = findViewById<EditText>(R.id.inputAnswer)
        data = (application as AppSingleton).data
        quiz = Quiz(data)
        editText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (!quiz.done){
                    submitAnswer(editText)
                }
                return@OnKeyListener true
            }
            false
        })
        runRound()
    }

    private fun runRound(){
        val imageView = findViewById<ImageView>(R.id.imageViewGuess)
        val person = quiz.pickPerson()
        imageView.setImageBitmap(person.image)
    }

    fun restart(view: View){
        quiz = Quiz(data)
        findViewById<Button>(R.id.buttonRestart).visibility = View.INVISIBLE
        findViewById<Button>(R.id.btnSubmit).visibility = View.VISIBLE
        findViewById<TextView>(R.id.textPersons).text = "0"
        findViewById<TextView>(R.id.textScore).text = "0"
        runRound()
    }

    fun submitAnswer(view: View){
        val answerEdit = findViewById<EditText>(R.id.inputAnswer)
        val answer: String = answerEdit.text.toString()
        val attempts = findViewById<TextView>(R.id.textPersons)
        val score = findViewById<TextView>(R.id.textScore)
        quiz.answer(answer)
        attempts.text = quiz.attempts.toString()
        score.text = quiz.score.toString()
        answerEdit.setText("")
        if (!quiz.done){
            runRound()
        } else {
            Toast.makeText(this, "Well played!", Toast.LENGTH_SHORT).show()
            btnSubmit.visibility = View.INVISIBLE
            buttonRestart.visibility = View.VISIBLE
            inputAnswer.hideKeyboard()
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
