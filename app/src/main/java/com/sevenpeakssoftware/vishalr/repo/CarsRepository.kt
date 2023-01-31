package com.sevenpeakssoftware.vishalr.repo

import com.sevenpeakssoftware.vishalr.db.CarDbDao
import com.sevenpeakssoftware.vishalr.model.CarUiModel
import com.sevenpeakssoftware.vishalr.service.CarApiService
import javax.inject.Inject

class CarsRepository @Inject constructor(
    private val service: CarApiService,
    private val carUseCase: CarDataUseCase,
    private val carDao: CarDbDao
) {
    suspend fun downloadListOfCar(): CarAPIResponse {
        return try {
            val call = service.carsApi()
            if (call.isSuccessful) {
                val cars = carUseCase.intoUiModel(call.body())
                ApiSuccess(cars)
            } else {
                ApiFailed
            }
        } catch (sException: Exception) {
            SocketTimeout
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

// API Response sealed status
sealed class CarAPIResponse
data class ApiSuccess(val cars: List<CarUiModel>?) : CarAPIResponse()
object ApiFailed : CarAPIResponse()
object SocketTimeout : CarAPIResponse()