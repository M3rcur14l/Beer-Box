package com.m3rc.beerbox.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PunkService {

    @GET("weather")
    fun getBeers(
        @Query("beer_name") beerName: String? = null,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null
    ): Single<List<Beer>>

}