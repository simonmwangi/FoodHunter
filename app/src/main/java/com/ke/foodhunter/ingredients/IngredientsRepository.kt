package com.ke.foodhunter.ingredients

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ke.foodhunter.data.FirebaseCallback
import com.ke.foodhunter.data.Ingredient
import com.ke.foodhunter.data.Response

class IngredientsRepository (
    private val ingredientRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Ingredients-List")
){
    fun getResponseFromRealtimeDatabaseUsingCallback(callback: FirebaseCallback) {
        Log.v("Repo Call","1" )
        ingredientRef.get().addOnCompleteListener { task ->
            val response = Response()
            if (task.isSuccessful) {
                val result =  task.result


                val array = arrayOf("Sugar", "Salt", "Vitamin C")
                var list = mutableListOf<Ingredient>()
                array.forEach {

                    if (task.result.hasChild(it)){
                        val key = result.child(it).key.toString()
                        val value = result.child(it).value.toString()
                        list += Ingredient(key, value)
                        Log.i("Vars","Variables to save: Before: $key <> $value After: $list")
                    }
                }
                response.ingredients = list


                Log.v("Repo Call","2" )


                Log.v("Repo Call","3 ${task.result.child("Sugar").value}" )

                /*
                * Can Prove Useful Sometime Later
                result?.let {

                    response.ingredients = result.children.map { snapShot ->
                        //snapShot.getValue(Ingredient::class.java)!!

                        val key = snapShot.key.toString()
                        val value = snapShot.value.toString()

                        Ingredient(key,value)
                    }
                }
                */

            } else {
                response.exception = task.exception
                Log.v("Repo Call","4" )
            }
            callback.onResponse(response)
        }
        Log.v("Repo Call","4" )
    }



 }