package com.m3rc.beerbox.data

import com.google.gson.annotations.SerializedName

data class Beer(
    val name: String?,
    @SerializedName("tagline") val tagLine: String?,
    val description: String?,
    @SerializedName("image_url") val image: String?
)