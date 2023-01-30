package com.sevenpeakssoftware.vishalr.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sevenpeakssoftware.vishalr.model.CarUiModel
import kotlinx.coroutines.CoroutineScope

@Database(entities = [CarUiModel::class], version = 1, exportSchema = false)
abstract class CarOfflineRoomDatabase : RoomDatabase() {

    abstract fun carDao(): CarDbDao

    companion object{
        @Volatile
        private var INSTANCE: CarOfflineRoomDatabase? = null

        fun getDbInstance(
            context: Context
        ): CarOfflineRoomDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    CarOfflineRoomDatabase::class.java,
                    "car_db")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}