package com.m3rc.beerbox.widget.searchview

import io.reactivex.Single

/**
 * Created by Antonello Fodde on 12/10/2017.
 * antonello.fodde@accenture.com
 */
interface QuerySuggestionProvider {
    fun getSuggestions(input: String): Single<List<Suggestion>>
}