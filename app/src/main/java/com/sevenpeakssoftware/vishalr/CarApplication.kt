package com.sevenpeakssoftware.vishalr

import android.app.Application
import com.sevenpeakssoftware.vishalr.di.CarApplicationComponent
import com.sevenpeakssoftware.vishalr.di.DaggerCarApplicationComponent

class CarApplication : Application() {
    lateinit var appComponent: CarApplicationComponent
    override fun onCreate() {
        super.onCreate()
        // Initialise app component here
        appComponent = DaggerCarApplicationComponent.factory().create(applicationContext)
    }
}