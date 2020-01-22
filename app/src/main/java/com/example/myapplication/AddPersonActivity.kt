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


class AddPersonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_person)
    }

    fun pickPhoto(view: View){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    fun takePhoto(view: View){
        dispatchTakePictureIntent()
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    fun addPerson(view: View){
        val nameInput = findViewById<EditText>(R.id.inputName)
        val name: String = nameInput.text.toString()
        val image: ImageView = findViewById<ImageView>(R.id.imageView)
        val bitmap: Bitmap = (image.drawable as BitmapDrawable).bitmap
        val person = Person(name, bitmap)
        val data = (application as AppSingleton).data
        data.add(person)
        Toast.makeText(this, "$name added to database!", Toast.LENGTH_SHORT).show()
        nameInput.setText("")
        image.setImageDrawable(null)
    }

    fun checkIfFieldsAreValid(){
        val name: String = findViewById<EditText>(R.id.inputName).text.toString()
        val imageView = findViewById<ImageView>(R.id.imageView)
        val addButton = findViewById<Button>(R.id.buttonAdd)
        addButton.isClickable = name.length > 2 && imageView.drawable != null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageView = findViewById<ImageView>(R.id.imageView)
        if (resultCode == Activity.RESULT_OK && requestCode == 1000){
            imageView.setImageURI(data?.data)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            val imageBitmap: Bitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }
}
