package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.Entity.PersonEntity
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {
    lateinit var quiz: Quiz
    lateinit var data: ArrayList<PersonEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        val personViewModel = ViewModelProvider(this).get(PersonViewModel::class.java)
        data = personViewModel.getPeopleList()
        quiz = Quiz(data)
        // enables the user to press enter to submit an answer
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

    /**
     * called at the start of each round
     * will get the next person and display the image
     */
    private fun runRound(){
        val person = quiz.pickPerson()
        Glide.with(imageViewGuess.context)
            .load(person.image)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
            .into(imageViewGuess)
    }

    /**
     * Will restart the game
     */
    fun restart(view: View){
        quiz = Quiz(data)
        buttonRestart.visibility = View.INVISIBLE
        btnSubmit.visibility = View.VISIBLE
        textPersons.text = "0"
        textScore.text = "0"
        runRound()
    }

    /**
     * Called when user submits answer by pressing enter or the dedicated button
     * Will check if answer is correct, display feedback and check if the game is done
     */
    fun submitAnswer(view: View){
        val answerFeedback = quiz.answer(inputAnswer.text.toString())
        textPersons.text = quiz.attempts.toString()
        textScore.text = quiz.score.toString()
        displayFeedback(answerFeedback)
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

    /**
     * Used to display feedback on the users answers
     * @param isCorrect contains info about the answers correctness
     */
    private fun displayFeedback(answerFeedback: AnswerFeedback){
       if(answerFeedback.isCorrect) {
           textFeedback.text = ""
           textFeedback.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_24dp, 0, 0, 0)
       } else {
           textFeedback.text = answerFeedback.correctName
           textFeedback.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_24dp, 0, 0, 0)
       }
        textFeedback.visibility = View.VISIBLE
        textFeedback.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.left_right))
        textFeedback.visibility = View.INVISIBLE
    }

    /**
     * Used to hide keyboard when the quiz is complete
     */
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
