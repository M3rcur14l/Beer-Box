package com.m3rc.beerbox.widget.searchview

import io.reactivex.Observable

/**
 * Created by Antonello Fodde on 12/10/2017.
 * antonello.fodde@accenture.com
 */
interface VoiceSearchProvider {
    val voiceSearch: Observable<String>
    val requestCode: Int
}
