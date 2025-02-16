package com.example.auraprototype.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClickedItem(
    @SerializedName("_id") val id: String? = "",
    @SerializedName("faceshape") val faceShape: String? = "",
    @SerializedName("beardstyle") val name: String? ="",
    @SerializedName("image") val image: String? ="",
    @SerializedName("description") val description: String? ="",
    @SerializedName("__v") val version: Int? = 0,
    @SerializedName("gender") val gender: String? = ""
) : Parcelable