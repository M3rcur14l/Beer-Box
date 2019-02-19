package com.m3rc.beerbox.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Beer(
    val id: Long?,
    val name: String?,
    @SerializedName("tagline") val tagLine: String?,
    val description: String?,
    @SerializedName("image_url") val image: String?,
    val ebc: Float?
) : Parcelable