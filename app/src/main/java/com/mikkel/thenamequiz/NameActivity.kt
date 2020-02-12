package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_name.*

class NameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)
    }

    fun onSubmit(view: View) {
        val name : String = editName.text.toString()
        val sharedPref = this.getSharedPreferences(getString(R.string.prefKey), Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("username", name)
            commit()
        }
        Toast.makeText(this, "Added $name as username", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, MainActivity::class.java))
    }
}