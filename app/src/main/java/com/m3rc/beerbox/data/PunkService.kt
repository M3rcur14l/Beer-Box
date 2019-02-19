package com.m3rc.beerbox.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PunkService {

    @GET("beers")
    fun getBeers(
        @Query("beer_name") beerName: String? = null,
        @Query("ebc_gt") ebcLowerBound: Int? = null,
        @Query("ebc_lt") ebcUpperBound: Int? = null,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null
    ): Single<List<Beer>>

}