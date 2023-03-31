package com.ke.foodhunter.recipes

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

fun main(){
    searchRecipes()
}

fun downloadUrlAsync( url: String) {
    val data: String?
    val myUrl = URL(url)
        val conn = myUrl.openConnection() as HttpURLConnection
        conn.setRequestProperty("Accept", "application/json")
        try {
            data = conn.inputStream.use { it.reader().use{reader -> reader.readText()} }
            Log.i("Data Stream", data)

        } catch (ex: Exception) {
            Log.d("Exception", ex.toString())
        }
}

private fun searchRecipes() {

    val searchWord = "eggs"
    val yourAppId = "a05fced1"
    val yourAppKey = "904ac86d76d3935cde232791c2491bee"
    val min = 1
    val max = 5
    val url = "https://api.edamam.com/search?q=${searchWord}&app_id=${yourAppId}&app_key=${yourAppKey}&&from=${min}&to=${max}&calories=591-722&health=alcohol-free"
    /*downloadUrlAsync(this, url) { it ->

        val mp = ObjectMapper()
        val myObject: RecipeJsonObject= mp.readValue(it, RecipeJsonObject::class.java)
        val recipes: MutableList<Hit>? = myObject.hits

        // Add new recipes to list
        recipes?.forEach() {
            it.recipe
        }
    }*/
    downloadUrlAsync(url)
}



