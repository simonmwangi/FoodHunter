package com.ke.foodhunter

import com.google.firebase.database.*
import org.jsoup.Jsoup


data class WebScraperInfo(
    val title: String,
    val description: String
): java.io.Serializable


class WebScraper () {

    private lateinit var database: DatabaseReference

    //Creating member variables
    private var mFirebaseDatabase: DatabaseReference?=null
    private var mFirebaseInstance: FirebaseDatabase?=null


    fun scrapFromWeb(searchArray: MutableList<String>): List<WebScraperInfo> {
        val ingredientsList = mutableListOf<WebScraperInfo>()
        // Enter the name of the compound or nutrient to search for
        //val searchQuery = "Vitamin D"
        for (searchQuery in searchArray){
            // Set up the URL for the search query
            val url = "https://foodb.ca/unearth/q?button=&query=${
                searchQuery.replace(
                    " ",
                    "+"
                )
            }&searcher=compounds"

            // Connect to the website and get the search results
            val doc = Jsoup.connect(url).get()
            //print(doc)
            // Find the first search result (assuming it is the most relevant)
            val resultLink = doc.select(".result-link .btn-card ").first()?.attr("href")
            print("The result link is: ${resultLink}")
            //Now to obtain the result of the compound
            val resultDoc = Jsoup.connect("https://foodb.ca${resultLink}").get()
            //print(resultDoc)
            val description = resultDoc.select("tbody :eq(8)").first()?.child(1)?.text()
            println(description)
            // Connect to the page for the search result
            //val resultDoc = resultLink?.let { Jsoup.connect(it).get() }
            //print(resultLink)

            ingredientsList += (WebScraperInfo(searchQuery,description?: "None Found"))
        }
        return ingredientsList
    }
}
fun main(){
    val searchList = arrayOf("Flour","Calcium Sulphate" )
    //val test = WebScraper()
    //println(test.scrapFromWeb(searchList))
    //println(WebScraper().scrapFromDatabase(searchList, mFirebaseDatabase))
}


    /* Find the nutrient data for the compound or nutrient
    val nutrientData = resultDoc.select("table.nutrient-table tr")

    Extract the nutrient values for each nutrient
    for (row in nutrientData) {
        val nutrient = row.select("th").text()
        val value = row.select("td").text()
        println("$nutrient: $value")
    }


}
*/