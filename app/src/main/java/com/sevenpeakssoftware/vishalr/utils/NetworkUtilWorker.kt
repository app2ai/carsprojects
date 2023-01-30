package com.sevenpeakssoftware.vishalr.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sevenpeakssoftware.vishalr.db.CarOfflineRoomDatabase
import com.sevenpeakssoftware.vishalr.repo.ApiFailed
import com.sevenpeakssoftware.vishalr.repo.ApiSuccess
import com.sevenpeakssoftware.vishalr.repo.CarDataUseCase
import com.sevenpeakssoftware.vishalr.repo.CarsRepository
import com.sevenpeakssoftware.vishalr.service.RetrofitClient

class NetworkUtilWorker(
    appCtx: Context,
    workParam: WorkerParameters
) : CoroutineWorker(appCtx, workParam) {
    override suspend fun doWork(): Result {
        Log.d("Connected", "doWork: Downloading cars")
        val service = RetrofitClient.sportService
        val useCase = CarDataUseCase()
        val db = CarOfflineRoomDatabase.getDbInstance(applicationContext)
        val repo = CarsRepository(service, useCase, db.carDao())
        val status = repo.downloadListOfCar()
        return when(status) {
            is ApiSuccess -> {
                val cars = status.cars?.reversed() ?: listOf()
                for (car in cars) {
                    db.carDao().addCar(car)
                }
                Result.success()
            }
            ApiFailed -> {
                Result.failure()
            }
        }
    }
}