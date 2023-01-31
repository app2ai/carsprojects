package com.sevenpeakssoftware.vishalr.repo

import com.sevenpeakssoftware.vishalr.db.CarDbDao
import com.sevenpeakssoftware.vishalr.model.CarModel
import com.sevenpeakssoftware.vishalr.model.CarUiModel
import com.sevenpeakssoftware.vishalr.service.CarApiService
import java.net.SocketTimeoutException
import javax.inject.Inject

class CarsRepository @Inject constructor(
    private val service: CarApiService,
    private val carUseCase: CarDataUseCase,
    private val carDao: CarDbDao
) {
    suspend fun downloadListOfCar() : CarAPIResponse {
        try {
            val call = service.carsApi()
            if (call.isSuccessful) {
                val cars = carUseCase.intoUiModel(call.body())
                return ApiSuccess(cars)
            } else {
                return ApiFailed
            }
        } catch (sException: Exception) {
            return SocketTimeout
        }
    }

    suspend fun getAllCars(): List<CarUiModel> {
        return carDao.getAllCars() ?: listOf()
    }

    suspend fun saveCarsInDb(cars: List<CarUiModel>) {
        for (car in cars) {
            carDao.addCar(car)
        }
    }
}

sealed class CarAPIResponse
    data class ApiSuccess(val cars: List<CarUiModel>?): CarAPIResponse()
    object ApiFailed: CarAPIResponse()
    object SocketTimeout: CarAPIResponse()