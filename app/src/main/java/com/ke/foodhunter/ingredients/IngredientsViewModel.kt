package com.ke.foodhunter.ingredients

import androidx.lifecycle.ViewModel
import com.ke.foodhunter.data.FirebaseCallback

class IngredientsViewModel (
    private val repository: IngredientsRepository = IngredientsRepository()
    ): ViewModel() {
        fun getResponseUsingCallback(callback: FirebaseCallback) {
            repository.getResponseFromRealtimeDatabaseUsingCallback(callback)
        }
}