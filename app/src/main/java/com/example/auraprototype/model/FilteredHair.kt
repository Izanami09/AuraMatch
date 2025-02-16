package com.example.auraprototype.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class FilteredHair(
    @SerializedName("_id") val id: String?,
    @SerializedName("faceshape") val faceShape: String?,
    @SerializedName("hairstyle") val hairStyle: String?, // Add `hairstyle` if expected
    @SerializedName("image") val image: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("__v") val version: Int?,
    @SerializedName("gender") val gender: String?
) : Parcelable
