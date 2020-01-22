package com.example.myapplication

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toDrawable

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openDbActivity(view: View) {
        val intent = Intent(this, DatabaseActivity::class.java)
        startActivity(intent)
    }

    fun openAddPersonActivity(view: View) {
        val intent = Intent(this, AddPersonActivity::class.java)
        startActivity(intent)
    }

    fun openQuizActivity(view: View) {
        val intent = Intent(this, QuizActivity::class.java)
        startActivity(intent)
    }
}
