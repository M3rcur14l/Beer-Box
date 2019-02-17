package com.m3rc.beerbox.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PunkService {

    @GET("weather")
    fun getBeers(@Query("beer_name") beerName: String?): Single<List<Beer>>

}