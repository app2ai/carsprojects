package com.sevenpeakssoftware.vishalr.di

import com.sevenpeakssoftware.vishalr.service.CarApiService
import com.sevenpeakssoftware.vishalr.service.RetrofitClient
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    fun provideCarApiService(): CarApiService {
        return RetrofitClient.sportService
    }
}