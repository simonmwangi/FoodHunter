package com.ke.foodhunter.user.details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class CombinedData (
    var userChoice: String = "",
    var username: String = "UserX",
    var height: Float = 0f,
    var weight: Float = 0f,
    var ailments: String = "None",
    var restrictions: String = "None",
    var familyNo: Int = 1,
    var mainGoal : String = ""
    ) : Parcelable