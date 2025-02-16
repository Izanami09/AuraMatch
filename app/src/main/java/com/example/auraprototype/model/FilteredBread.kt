package com.example.auraprototype.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class FilteredBread(
    @SerializedName("_id") val id: String?,
    @SerializedName("faceshape") val faceShape: String?,
    @SerializedName("beardstyle") val beardStyle: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("__v") val version: Int?,
    @SerializedName("gender") val gender: String?
) : Parcelable