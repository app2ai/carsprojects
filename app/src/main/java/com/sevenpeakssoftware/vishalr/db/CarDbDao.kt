package com.sevenpeakssoftware.vishalr.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sevenpeakssoftware.vishalr.model.CarUiModel

@Dao
interface CarDbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCar(user: CarUiModel): Long

    @Query("Select * from tblCar")
    suspend fun getAllCars(): List<CarUiModel>?
}