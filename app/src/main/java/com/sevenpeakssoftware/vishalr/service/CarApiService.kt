package com.sevenpeakssoftware.vishalr.service

import com.sevenpeakssoftware.vishalr.model.CarModel
import retrofit2.Response
import retrofit2.http.GET

interface CarApiService {

    @GET("/carlist")
    suspend fun carsApi(): Response<CarModel?>
}
