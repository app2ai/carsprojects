package com.sevenpeakssoftware.vishalr.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.sevenpeakssoftware.vishalr.db.CarOfflineRoomDatabase
import com.sevenpeakssoftware.vishalr.model.CarUiModel
import com.sevenpeakssoftware.vishalr.repo.CarDataUseCase
import com.sevenpeakssoftware.vishalr.repo.CarsRepository
import com.sevenpeakssoftware.vishalr.service.RetrofitClient
import kotlinx.coroutines.launch

class CarListViewModel : ViewModel() {
    private var _carsLiveData = MutableLiveData<List<CarUiModel>>()
    val carsLiveData: LiveData<List<CarUiModel>> = _carsLiveData

    private var _progressLiveData = MutableLiveData<Boolean>()
    val progressLiveData: LiveData<Boolean> = _progressLiveData

    fun getAllCarsLocally(context: Context) {
        viewModelScope.launch {
            val service = RetrofitClient.sportService
            val usecase = CarDataUseCase()
            val db = CarOfflineRoomDatabase.getDbInstance(context)
            val repo = CarsRepository(service, usecase, db.carDao())
            _carsLiveData.value = repo.getAllCars()
        }
    }

    fun setProgressVisibility(isProgress: Boolean) {
        _progressLiveData.value = isProgress
    }
}