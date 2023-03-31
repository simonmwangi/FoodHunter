package com.ke.foodhunter.user.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CombinedDataViewModel: ViewModel() {
    var userData by mutableStateOf<CombinedData?>(null)
        private set

    fun addToUserData(addData: CombinedData){
        userData = addData
    }



}