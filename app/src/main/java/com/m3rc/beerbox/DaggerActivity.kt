package com.m3rc.beerbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class DaggerActivity : AppCompatActivity(), HasFragmentInjector, HasSupportFragmentInjector {

    @JvmField
    @Inject
    var supportFragmentInjector: DispatchingAndroidInjector<Fragment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (shouldBeAndroidInjected())
            AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return supportFragmentInjector
    }

    protected fun shouldBeAndroidInjected(): Boolean {
        return true
    }
}
