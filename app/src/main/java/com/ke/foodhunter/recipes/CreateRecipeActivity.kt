package com.ke.foodhunter.recipes

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ke.foodhunter.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CreateRecipeActivity : ComponentActivity() {
    private val database = Firebase.database.reference
    private val storage = Firebase.storage.reference

    private val cameraRequestCode = 1
    private val galleryRequestCode = 2
    private var imageUri: Uri? = null
    private var selectedImageUris = mutableStateListOf<Uri>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateRecipeUI()
        }
    }

    @Composable
    fun TagList(tags: List<String>, onTagsSelected: (Set<String>) -> Unit) {
        var selectedTags by remember { mutableStateOf(emptySet<String>()) }
        LazyRow {
            items(tags.size) { tag ->
                Tag(tags[tag], selectedTags) { clickedTag ->
                    if (selectedTags.contains(clickedTag)) {
                        selectedTags = selectedTags - clickedTag
                    } else {
                        selectedTags = selectedTags + clickedTag
                    }
                    onTagsSelected(selectedTags)
                }
            }
        }
    }


    @Composable
    fun Tag(tag: String, selectedTags: Set<String>, onTagClick: (String) -> Unit) {
        val isSelected = selectedTags.contains(tag)
        val backgroundColor = if (isSelected) Color.Green else Color.LightGray
        Surface(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            shape = RoundedCornerShape(16.dp),
            color = backgroundColor
        ) {
            Text(
                text = tag,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .clickable { onTagClick(tag) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }





    @Composable
    fun OpenGetImageDialog(onDismiss: () -> Unit) {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        var selectedOption by remember { mutableStateOf("None") }
        Dialog(
            onDismissRequest = {
                onDismiss()
            }
        ){
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                for(option in options){
                    Button(onClick = {
                        selectedOption = option
                    }) {
                        Text(text = option)
                    }
                }
            }
        }
        when (selectedOption) {
            options[0] -> {
                onDismiss()

                Toast.makeText(this@CreateRecipeActivity,selectedOption,Toast.LENGTH_SHORT).show()
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(packageManager) != null) {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    if (photoFile != null) {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "${BuildConfig.APPLICATION_ID}.fileprovider",
                            photoFile
                        )
                        imageUri = photoURI
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, cameraRequestCode)
                    }
                }
            }
            options[1] -> {
                Toast.makeText(this@CreateRecipeActivity,selectedOption,Toast.LENGTH_SHORT).show()
                onDismiss()
                //val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                //startActivityForResult(intent, galleryRequestCode)
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), galleryRequestCode)

            }
            options[2] -> {
                Toast.makeText(this@CreateRecipeActivity,selectedOption,Toast.LENGTH_SHORT).show()
                onDismiss()
            }

        }

    }

    @Composable
    fun CreateRecipeUI() {
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var ingredients by remember { mutableStateOf("") }
        var steps by remember { mutableStateOf("") }

        var openImageDialog by remember { mutableStateOf(false) }

        val viewModel: MyViewModel = viewModel()

        val tags = listOf("Healthy", "Vegetarian", "Easy", "Dinner")
        var `health-tags` by remember { mutableStateOf("") }

        fun clearFields() {
            title = ""
            description = ""
            ingredients = ""
            steps = ""
            imageUri = null
            selectedImageUris.clear()
        }


        Column {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Recipe Title") }
            )

            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Recipe Description") }
            )

            TextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = { Text("Ingredients") }
            )

            TextField(
                value = steps,
                onValueChange = { steps = it },
                label = { Text("Steps") }
            )
            TagList(tags) { selectedTag ->
                `health-tags` = selectedTag.joinToString(" ")
            }
            TextField(value = `health-tags`, onValueChange = { `health-tags` = it })

            Button(
                onClick = {
                    val recipeKey = database.child("recipes/local/under_review").push().key
                    if (recipeKey != null) {
                        // Upload recipe data to Realtime Database
                        val recipeData = mapOf(
                            "title" to title,
                            "description" to description,
                            "ingredients" to ingredients,
                            "steps" to steps
                        )
                        database.child("recipes/local/under_review").child(recipeKey).setValue(recipeData).addOnSuccessListener {
                            if (selectedImageUris.isNotEmpty()) {

                                val recipeId = "$recipeKey.jpg"
                                val storageRef = storage.child("Local Recipes Images")

                                //Will store the names of the names of the uploaded images to the recipe child
                                val imageUrls = mutableListOf<String>()
                                selectedImageUris.forEachIndexed { index, imageUri ->

                                    //Save each image in a folder with the same name as the recipe
                                    //Eases displaying them in the RecipeList LazyColumn
                                    val imageRef = storageRef.child(recipeKey).child("$index.jpg")
                                    val uploadEachImage = imageRef.putBytes(compressImage(imageUri, 50))

                                    uploadEachImage.addOnSuccessListener {taskSnapshot ->
                                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                                            imageUrls.add(it.toString())
                                            Log.i("URIs", imageUrls.toString())
                                            if (imageUrls.size == selectedImageUris.size) {
                                                //For all successful upload
                                                database.child("recipes/local/under_review")
                                                    .child(recipeKey).child("images").setValue(imageUrls).addOnSuccessListener {
                                                        Toast.makeText(this@CreateRecipeActivity, "Recipe shared successfully!", Toast.LENGTH_SHORT).show()
                                                        //Updates the UI for the next recipe addition
                                                        clearFields()
                                                    }.addOnFailureListener {
                                                        Toast.makeText(this@CreateRecipeActivity,"There was an error when uploading your images \n it", Toast.LENGTH_SHORT).show()
                                                    }
                                            }

                                        }

                                    }.addOnFailureListener {
                                        Toast.makeText(this@CreateRecipeActivity, "Error uploading image.", Toast.LENGTH_SHORT).show()
                                    }
                                }



                            }

                        }.addOnFailureListener{
                            Toast.makeText(this@CreateRecipeActivity, "Failed to upload recipe with error \n $it", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        Toast.makeText(this@CreateRecipeActivity, "Couldn't upload the recipe \n Check your internet connection.", Toast.LENGTH_SHORT).show()
                        //clearFields()
                    }
                }
            ) {
                Text("SHARE")
            }

            if(selectedImageUris.isNotEmpty()) {
                Text("Images (${selectedImageUris.size})")
                LazyHorizontalGrid(
                    rows = GridCells.Adaptive(100.dp),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp),
                    modifier = Modifier.height(100.dp)
                ) {
                    items(selectedImageUris.size) { index ->
                        Image(
                            painter = rememberImagePainter(data = selectedImageUris[index]),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                }
            }else{
                Text("No image selected.")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    openImageDialog = true
                }
            ) {
                Text("Add Image")
            }
        }

        if (openImageDialog){
            OpenGetImageDialog{
                openImageDialog = false
            }
        }


    }

    private fun compressImage(imageUri: Uri, quality: Int): ByteArray {
        val inputStream = contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

        return outputStream.toByteArray()
    }

    private var currentPhotoPath: String? = null

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                cameraRequestCode -> {
                    val file = File(currentPhotoPath)
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "${BuildConfig.APPLICATION_ID}.fileprovider",
                        file
                    )
                    //imageUri = photoURI
                    selectedImageUris.add(photoURI)
                }
                galleryRequestCode -> {
                    //First clear the List before adding new entries
                    //selectedImageUris.clear()

                    if (data?.clipData != null) {
                        for (i in 0 until data.clipData!!.itemCount) {
                            val imageUri = data.clipData!!.getItemAt(i).uri
                            selectedImageUris.add(imageUri)
                        }

                    } else {
                        val imageUri = data?.data
                        if (imageUri != null) {
                            selectedImageUris.add(imageUri)
                        }
                    }
                }
            }
        }

    }

    @Composable
    @Preview(showBackground = true, showSystemUi = true)
    fun CreateRecipePreview(){
        CreateRecipeUI()
    }

}

class MyViewModel : ViewModel() {
    // Dialog box
    var open = MutableLiveData<Boolean>()

    fun startThread() {
        viewModelScope.launch {

            withContext(Dispatchers.Default) {
                // Do the background work here
                // I'm adding delay
               // delay(3000)
            }

            closeDialog()
        }
    }

    private fun closeDialog() {
        open.value = false
    }
}


