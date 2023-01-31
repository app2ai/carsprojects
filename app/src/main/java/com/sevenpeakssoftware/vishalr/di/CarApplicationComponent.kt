package com.sevenpeakssoftware.vishalr.di

import android.content.Context
import com.sevenpeakssoftware.vishalr.MainActivity
import com.sevenpeakssoftware.vishalr.view.CarListFragment
import com.sevenpeakssoftware.vishalr.view.SplashFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class, DatabaseModule::class])
interface CarApplicationComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: CarListFragment)
    fun inject(fragment: SplashFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context) : CarApplicationComponent
    }
}