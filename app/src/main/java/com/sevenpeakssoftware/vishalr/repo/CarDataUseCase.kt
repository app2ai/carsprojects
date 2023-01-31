package com.sevenpeakssoftware.vishalr.repo

import com.sevenpeakssoftware.vishalr.model.CarModel
import com.sevenpeakssoftware.vishalr.model.CarUiModel
import javax.inject.Inject

// This is responsible for converting api data to ui data
class CarDataUseCase @Inject constructor() : DataConvertor<CarModel, CarUiModel> {
    override fun intoUiModel(dataClass: CarModel?): List<CarUiModel> {
        val listOfCar = mutableListOf<CarUiModel>()
        if (dataClass != null) {
            for (car in dataClass.content) {
                listOfCar.add(CarUiModel(
                    id = car.id,
                    title = car.title,
                    ingress = car.ingress,
                    image = car.image,
                    dateTime = car.dateTime
                ))
            }
        }
        return listOfCar
    }
}