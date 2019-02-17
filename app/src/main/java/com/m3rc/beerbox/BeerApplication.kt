package com.m3rc.beerbox

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.m3rc.beerbox.di.AppComponent
import com.m3rc.beerbox.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class BeerApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    override fun activityInjector() = activityInjector

}
