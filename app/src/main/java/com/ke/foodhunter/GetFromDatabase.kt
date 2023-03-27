package com.ke.foodhunter

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

fun main() {

    // Initialize Firebase app and database reference
    //val firebaseApp = FirebaseApp.initializeApp(
    //    FirebaseOptions.Builder()
    //    .setDatabaseUrl("https://your-firebase-database-url.firebaseio.com/")
    //    .build())
    val database = FirebaseDatabase.getInstance().reference

    // List of ingredient names to search for
    val searchIngredients = listOf("salt", "pepper", "sugar")

    // Search for each ingredient in the database and print results
    for (ingredient in searchIngredients) {
        database.child("ingredients").child(ingredient).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val title = dataSnapshot.key
                    val description = dataSnapshot.value as String
                    println("$title: $description")
                } else {
                    println("$ingredient not found")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("Database error: ${databaseError.message}")
            }
        })
    }
}
