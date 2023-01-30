package com.sevenpeakssoftware.vishalr.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblCar")
data class CarUiModel (
    @PrimaryKey
    val id: Int,
    val title: String,
    val ingress: String,
    val image: String,
    val dateTime: String
)