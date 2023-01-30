package com.sevenpeakssoftware.vishalr

import android.app.Application
import com.sevenpeakssoftware.vishalr.di.DaggerCarApplicationComponent

class CarApplication : Application() {
    val appComponent = DaggerCarApplicationComponent.create()
}