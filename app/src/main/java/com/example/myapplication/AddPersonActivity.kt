package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.Entity.PersonEntity
import kotlinx.android.synthetic.main.activity_add_person.*
import kotlinx.android.synthetic.main.activity_quiz.*


class AddPersonActivity : AppCompatActivity() {
    private var imageUri: Uri? = Uri.EMPTY
    private lateinit var personViewModel: PersonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_person)
        personViewModel = ViewModelProvider(this).get(PersonViewModel::class.java)
    }

    // used to distinguish between different activity results
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_PICK = 2
    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 3

    /**
     * picks photo from phone
     */
    fun pickPhoto(view: View){
        checkPermissions()
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
        }
    }

    private fun checkPermissions(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            pickPhotoIfPermissionOK()
        }

    }

    /**
     * captures photo using the camera
     */
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

    /**
     * called when user presses button to add the new person
     * will take bitmap from imageview, and the person name from the edittext
     * and use this information to create a new person and add it to the database
     */
    fun addPerson(view: View){
//        val bitmap: Bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val person = PersonEntity(name = inputName.text.toString(), picture = imageUri.toString())
//        val data = (application as AppSingleton).data
//        data.add(person)
        personViewModel.insert(person)
        Toast.makeText(this, "${inputName.text} added to database!", Toast.LENGTH_SHORT).show()
        inputName.setText("")
        imageView.setImageDrawable(null)
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
            val imageBitmap: Bitmap = data?.extras?.get("data") as Bitmap
            Glide.with((imageView))
                .load(imageBitmap)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
                .into(imageView)
        }
    }
}
