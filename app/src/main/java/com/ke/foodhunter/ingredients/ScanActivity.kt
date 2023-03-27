package com.ke.foodhunter.ingredients

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.ke.foodhunter.IngredientsInfoActivity
import com.ke.foodhunter.R
import com.ke.foodhunter.WebScraper
import com.ke.foodhunter.data.FirebaseCallback
import com.ke.foodhunter.data.Ingredient
import com.ke.foodhunter.data.Response


class ScanActivity : AppCompatActivity() {
    private lateinit var inputImageBtn: MaterialButton
    private lateinit var recognizeTextBtn: MaterialButton
    private lateinit var scrapDatabaseBtn: MaterialButton
    private lateinit var imageView: ImageView
    private lateinit var recognizedEditText: EditText

    private companion object {
        private const val CAMERA_REQUEST_CODE = 100
        private const val STORAGE_REQUEST_CODE = 101
    }

    //Uri for the image fetched from the camera/ gallery
    private var imageUri: Uri? = null

    //arrays for the permissions required to pick image from the camera/ gallery
    private lateinit var cameraPermissions: Array<String>
    private lateinit var storagePermissions: Array<String>

    private lateinit var progressDialog: ProgressDialog

    private lateinit var textRecognizer: TextRecognizer


    private lateinit var viewModel: IngredientsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        viewModel = ViewModelProvider(this)
            .get(IngredientsViewModel::class.java)


        //UI Views
        inputImageBtn = findViewById(R.id.get_img_btn)
        recognizeTextBtn = findViewById(R.id.recognize_txt_btn)
        scrapDatabaseBtn = findViewById(R.id.scrap_txt_btn)
        imageView = findViewById(R.id.image_view)
        recognizedEditText = findViewById(R.id.show_result_txt)

        //init arrays of permissions required for Camera & Gallery
        cameraPermissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)


        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        textRecognizer = TextRecognition.getClient()

        scrapDatabaseBtn.setOnClickListener {
            showToast("Clicked...")
            val testData = arrayOf("Sugar","Salt")

            getResponseUsingCallback(testData)
        }
        //On click
        inputImageBtn.setOnClickListener{
            showInputImageDialog()
        }

        recognizeTextBtn.setOnClickListener {
            if(imageUri == null){
                showToast("Pick Image First")
            }else{
                recognizeTextFromImage()
            }
        }
    }

    //
    private fun getResponseUsingCallback(searchList: Array<String>) {

        viewModel.getResponseUsingCallback(object : FirebaseCallback {
            override fun onResponse(response: Response) {
                printResponseFromFirebase(response)
            }
        },searchList)

    }

    //This function gets the response from the async call and checks if the data is empty
    private fun printResponseFromFirebase(response: Response) {
        progressDialog.setMessage("Searching Online...")
        val ingredients = response.ingredients
        val remainder = response.remainingList

        if (remainder?.isEmpty() == true){
            ingredients?.let {
                progressDialog.dismiss()
                displayIngredientsInformation(it) }
        }
        else {
            progressDialog.setMessage("Still searching...\n might take a little longer")
            //To avoid an exception thrown when the application tries to perform a network operation
            //on the main thread
            //Alternative is calling the network operation in a thread or asynch class
            val policy = ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)
            //Search the remaining ingredients in the Web Scraper
            val fromScraperList = WebScraper().scrapFromWeb(remainder as MutableList<String>)
            //then add them together with the found data

            val combinedIngredientList = ingredients?.plus(fromScraperList)
            if (combinedIngredientList != null) {
                progressDialog.dismiss()
                displayIngredientsInformation(combinedIngredientList)
            }else{
                progressDialog.dismiss()
                displayIngredientsInformation(null)
            }
        }
        response.ingredients?.let { list ->
            list.forEach{ item ->

                Log.i("Each Iterable","${item.title} <> ${item.description}")
            }
        }

        //When a failure happens when fetching the data from the query
        response.exception?.let { exception ->
            exception.message?.let {
                Log.e("Test", it)
            }
        }
    }

    //Display the collected list to IngredientsInfoActivity
    private fun displayIngredientsInformation(ingredients: List<Ingredient>?) {
        if (ingredients.isNullOrEmpty()){
            showToast("No ingredients were found in the database")
        }
        else{
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            val intent = Intent(this, IngredientsInfoActivity::class.java)
            intent.putExtra("list", ingredients as java.io.Serializable)
            startActivity(intent)   //Opens the IngredientsInfoActivity with the ingredients list
        }

    }

    private fun recognizeTextFromImage(){
        progressDialog.setMessage("Preparing Image...")
        progressDialog.show()

        var myIngredients = arrayOf<String>()

        try {
            val inputImage = InputImage.fromFilePath(this, imageUri!!)

            progressDialog.setMessage("Recognizing Text...")

            val textTaskResult = textRecognizer.process(inputImage)
                .addOnSuccessListener { text ->

                    val recognizedText = text.text
                    for (block in text.textBlocks){
                        Log.v("Txt Block", block.text)
                        //Some Ingredients were found
                        if (block.text.contains("INGREDIENTS",ignoreCase = true)) {
                            val ingredients = block.text.drop(12)
                            //Means that the ingredients block has multiple items
                            if (ingredients.contains(',')){
                                myIngredients = ingredients.split(",").toTypedArray()

                                }
                            else{
                                //Has a single item
                                myIngredients[0] = ingredients
                            }

                            fun removeMultipleCharsFromString(str: String, charsToRemove: String): String {
                                val firstChar = StringBuilder(str)
                                val sb = StringBuilder()
                                if(str[0] == ' '){
                                    firstChar.deleteAt(0).toString()
                                }


                                for (element in firstChar) {
                                    if (!charsToRemove.contains(element)) {
                                        sb.append(element)
                                    }
                                }
                                return sb.toString()
                            }

                            var cleanedList = arrayOf<String>()
                            for(element in myIngredients){
                                cleanedList += (removeMultipleCharsFromString(element, "]#$[."))
                            }

                            //Calls the Callback function to start searching the ingredients list from the Firebase Database
                            getResponseUsingCallback(cleanedList)
                        }
                        else{
                            Log.v("No Ingredients", "Not Found" )
                            showToast("No ingredients were found: Try Again...")
                        }
                    }

                    recognizedEditText.setText(recognizedText)
                }
                .addOnFailureListener { error ->
                    progressDialog.dismiss()

                    showToast("Failed to recognize text due to ${error.message}")
                }
        }
        catch (e: Exception){
            progressDialog.dismiss()
            showToast("Failed to prepare image due to ${e.message}")
        }
    }

    private fun showInputImageDialog() {
        val popupMenu = PopupMenu(this, inputImageBtn)

        popupMenu.menu.add(Menu.NONE, 1, 1, "CAMERA")
        popupMenu.menu.add(Menu.NONE, 2, 2, "GALLERY")

        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { menuItem ->
            //get the clicked item id
            val id = menuItem.itemId
            //Camera Item was clicked
            if (id == 1){
                //Check CAMERA Permission
                if(checkCameraPermission()){
                    pickImageCamera()
                }else{
                    requestCameraPermission()
                }
            }
            else if(id ==2){
                if (checkStoragePermission()){
                    pickImageGallery()
                }else{
                    requestStoragePermission()
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)

        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private val galleryActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                imageUri = data!!.data

                imageView.setImageURI(imageUri)
            }
            else{
                showToast("Cancelled...")
            }
        }
    private fun pickImageCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Sample Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)
    }
    private val cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
            //receive the image if taken from camera
            if (result.resultCode == Activity.RESULT_OK){

                //show the image in the imageView
                imageView.setImageURI(imageUri)
            }
            else
            {
                showToast("Cancelled...")
            }
        }
    private fun checkStoragePermission(): Boolean{
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    }
    private fun checkCameraPermission(): Boolean {
        val cameraResult =  ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val storageResult = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        return cameraResult && storageResult
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE)
    }
    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()){
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted && storageAccepted){
                        pickImageCamera()
                    }
                    else{
                        showToast("Camera and Storage Permissions are required...")
                    }
                }
            }
            STORAGE_REQUEST_CODE -> {
                //Check the action performed in the permission dialog (Allow or Deny)
                if (grantResults.isNotEmpty()){
                    //Check if storage permission was granted
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                    if(storageAccepted){
                        pickImageGallery()
                    }
                    else {
                        showToast("Storage permission is required...")
                    }
                }
            }
        }
    }

    private fun showToast(message: String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}