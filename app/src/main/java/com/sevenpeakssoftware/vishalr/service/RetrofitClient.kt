package com.sevenpeakssoftware.vishalr.service

import com.sevenpeakssoftware.vishalr.utils.Constant.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

// Retrofit client object
@Singleton
object RetrofitClient {
    val sportService: CarApiService
        get(){
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CarApiService::class.java)
        }

    private val okHttpClient: OkHttpClient
        get(){
            return OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .build()
        }
}