package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.changeUsernameItem -> {
                startActivity(Intent(this, NameActivity::class.java))
                true
            }
            R.id.addNewItem -> {
                startActivity(Intent(this, DatabaseActivity::class.java))
                true
            }
            R.id.playItem -> {
                startActivity(Intent(this, QuizActivity::class.java))
                true
            }
            R.id.seeAllItem -> {
                startActivity(Intent(this, DatabaseActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref = this.getSharedPreferences(getString(R.string.prefKey), Context.MODE_PRIVATE)
        if (!sharedPref.contains("username")) {
            Toast.makeText(this, "Did not find username", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, NameActivity::class.java))
        }
        welcomeText.text = getString(R.string.welcome, sharedPref.getString("username", "username"))
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
