package com.m3rc.beerbox.bus.event

import com.m3rc.beerbox.data.Beer

data class NewBeerPageEvent(val beerList: List<Beer>)