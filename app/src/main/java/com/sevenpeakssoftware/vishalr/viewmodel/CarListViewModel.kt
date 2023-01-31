package com.sevenpeakssoftware.vishalr.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.sevenpeakssoftware.vishalr.db.CarOfflineRoomDatabase
import com.sevenpeakssoftware.vishalr.model.CarUiModel
import com.sevenpeakssoftware.vishalr.repo.ApiFailed
import com.sevenpeakssoftware.vishalr.repo.ApiSuccess
import com.sevenpeakssoftware.vishalr.repo.CarDataUseCase
import com.sevenpeakssoftware.vishalr.repo.CarsRepository
import com.sevenpeakssoftware.vishalr.repo.SocketTimeout
import com.sevenpeakssoftware.vishalr.service.RetrofitClient
import kotlinx.coroutines.launch
import javax.inject.Inject

class CarListViewModel @Inject constructor(
    private var repo: CarsRepository
) : ViewModel() {
    private var _carsLiveData = MutableLiveData<List<CarUiModel>>()
    val carsLiveData: LiveData<List<CarUiModel>> = _carsLiveData

    private var _progressLiveData = MutableLiveData<Boolean>()
    val progressLiveData: LiveData<Boolean> = _progressLiveData

    private var _isCarsUpdatedLiveData = MutableLiveData<Boolean>()
    val isCarsUpdatedLiveData: LiveData<Boolean> = _isCarsUpdatedLiveData

    fun getAllCarsLocally() {
        viewModelScope.launch {
            _carsLiveData.value = repo.getAllCars()
        }
    }

    fun setProgressVisibility(isProgress: Boolean) {
        _progressLiveData.value = isProgress
    }

    fun fetchCarsRemotely() {
        viewModelScope.launch {
            when(val responseStatus = repo.downloadListOfCar()) {
                is ApiSuccess -> {
                    repo.saveCarsInDb(responseStatus.cars ?: listOf())
                    _isCarsUpdatedLiveData.value = true
                }
                ApiFailed -> {
                    _isCarsUpdatedLiveData.value = false
                }
                SocketTimeout -> {
                    _isCarsUpdatedLiveData.value = false
                }
            }
        }
    }
}