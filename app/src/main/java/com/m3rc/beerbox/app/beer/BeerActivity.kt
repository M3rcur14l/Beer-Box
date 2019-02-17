package com.m3rc.beerbox.app.beer

import android.os.Bundle
import com.m3rc.beerbox.R
import com.m3rc.beerbox.app.BaseActivity

class BeerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer)
    }
}
