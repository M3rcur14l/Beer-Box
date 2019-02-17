package com.m3rc.beerbox.app

import kotlinx.android.synthetic.main.toolbar.*

abstract class BaseActivity : DaggerActivity() {

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

}
