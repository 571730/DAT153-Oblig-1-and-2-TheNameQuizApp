package com.example.myapplication

import android.app.Application
import android.graphics.BitmapFactory
import androidx.core.graphics.drawable.toDrawable

class AppSingleton : Application() {
    val data = ArrayList<Person>()
    override fun onCreate() {
        super.onCreate()
        data.add(Person("Donald Trump", BitmapFactory.decodeResource(resources, R.drawable.trump)))
        data.add(Person("Donald Trump jr", BitmapFactory.decodeResource(resources, R.drawable.trumpjr)))
    }
}