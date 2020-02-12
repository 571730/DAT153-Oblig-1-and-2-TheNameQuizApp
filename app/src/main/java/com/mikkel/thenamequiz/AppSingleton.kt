package com.example.myapplication

import android.app.Application
import android.graphics.BitmapFactory
import androidx.room.Room

/**
 * class is used as a database for the application
 * @property data contains the list of people used in the quiz
 * this list will be updated with more people, and people can be removed from this list
 */
class AppSingleton : Application() {
    val data = ArrayList<Person>()
    // populates the database with some initial data
    override fun onCreate() {
        super.onCreate()
        data.add(Person("Donald Trump", BitmapFactory.decodeResource(resources, R.drawable.trump)))
        data.add(Person("Donald Trump jr", BitmapFactory.decodeResource(resources, R.drawable.trumpjr)))
        data.add(Person("Barrack Obama", BitmapFactory.decodeResource(resources, R.drawable.barack_obama)))
    }
}