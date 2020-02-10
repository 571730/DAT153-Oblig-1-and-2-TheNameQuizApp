package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.Entity.PersonEntity
import kotlinx.android.synthetic.main.activity_add_person.*
import kotlinx.android.synthetic.main.activity_quiz.*
import java.io.File
import java.io.IOException
import java.util.*


class AddPersonActivity : AppCompatActivity() {
    private var imageUri: Uri? = Uri.EMPTY
    private lateinit var personViewModel: PersonViewModel

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
        setContentView(R.layout.activity_add_person)
        personViewModel = ViewModelProvider(this).get(PersonViewModel::class.java)
    }

    // used to distinguish between different activity results
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_PICK = 2
    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 3
    val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 4

    /**
     * picks photo from phone
     */
    fun pickPhoto(view: View){
        checkReadPermissions()
    }

    private fun pickPhotoIfPermissionOK(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("Ask permission", "Permission has been denied by user")
                    btnSubmit.isClickable = false
                } else {
                    Log.i("Ask permission", "Permission has been granted by user")
                    pickPhotoIfPermissionOK()
                }
            }
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("Ask permission", "Permission has been denied by user")
                    buttonCamera.isClickable = false
                } else {
                    Log.i("Ask permission", "Permission has been granted by user")
                    dispatchTakePictureIntent()
                }
            }
        }
    }

    private fun checkReadPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
        }
        else {
            // Permission has already been granted
            pickPhotoIfPermissionOK()
        }

    }

    private fun checkWritePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
        } else {
            dispatchTakePictureIntent()
        }
    }

    /**
     * captures photo using the camera
     */
    fun takePhoto(view: View){
        checkWritePermissions()
    }

    private fun dispatchTakePictureIntent() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.i("imgCapture", "Error while creating file for image capture")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "${BuildConfig.APPLICATION_ID}.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }

    }

    /**
     * called when user presses button to add the new person
     * will take bitmap from imageview, and the person name from the edittext
     * and use this information to create a new person and add it to the database
     */
    fun addPerson(view: View){
        val person = PersonEntity(name = inputName.text.toString(),
            picture = imageUri.toString())
        personViewModel.insert(person)
        Toast.makeText(this, "${inputName.text} added to database!", Toast.LENGTH_LONG).show()
        inputName.setText("")
        imageView.setImageResource(R.drawable.ic_face_24dp)
    }

    /**
     * handles the results from both the capture photo and pick photo
     * will in both cases add the bitmap to the imageview
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageView = findViewById<ImageView>(R.id.imageView)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_PICK){
            imageUri = data?.data
            Glide.with(imageView.context)
                .load(data?.data)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
                .into(imageView)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            Glide.with((imageView))
                .load(currentPhotoPath)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
                .into(imageView)
        }
    }

    private lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            imageUri = Uri.fromFile(this)
        }
    }

}
