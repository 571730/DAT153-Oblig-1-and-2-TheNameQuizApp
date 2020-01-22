package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_person.*


class AddPersonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_person)
    }

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_PICK = 2

    fun pickPhoto(view: View){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    fun takePhoto(view: View){
        dispatchTakePictureIntent()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    fun addPerson(view: View){
        val bitmap: Bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val person = Person(inputName.text.toString(), bitmap)
        val data = (application as AppSingleton).data
        data.add(person)
        Toast.makeText(this, "${inputName.text} added to database!", Toast.LENGTH_SHORT).show()
        inputName.setText("")
        imageView.setImageDrawable(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageView = findViewById<ImageView>(R.id.imageView)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_PICK){
            imageView.setImageURI(data?.data)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            val imageBitmap: Bitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }
}
