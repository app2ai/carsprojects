package com.sevenpeakssoftware.vishalr.repo

import com.sevenpeakssoftware.vishalr.db.CarDbDao
import com.sevenpeakssoftware.vishalr.model.CarModel
import com.sevenpeakssoftware.vishalr.model.CarUiModel
import com.sevenpeakssoftware.vishalr.service.CarApiService

class CarsRepository constructor(
    private val service: CarApiService,
    private val carUseCase: CarDataUseCase,
    private val carDao: CarDbDao
) {
    suspend fun downloadListOfCar() : CarAPIResponse {
        val call = service.carsApi()
        if (call.isSuccessful) {
            return ApiSuccess(carUseCase.intoUiModel(call.body()))
        } else {
            return ApiFailed
        }
    }

    suspend fun getAllCars(): List<CarUiModel> {
        return carDao.getAllCars() ?: listOf()
    }
}

sealed class CarAPIResponse
    data class ApiSuccess(val cars: List<CarUiModel>?): CarAPIResponse()
    object ApiFailed: CarAPIResponse()