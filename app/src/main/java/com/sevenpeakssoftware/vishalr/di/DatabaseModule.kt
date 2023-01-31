package com.sevenpeakssoftware.vishalr.di

import android.content.Context
import com.sevenpeakssoftware.vishalr.db.CarDbDao
import com.sevenpeakssoftware.vishalr.db.CarOfflineRoomDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
    @Provides
    fun provideCarDbDao(context: Context): CarDbDao {
        return CarOfflineRoomDatabase.getDbInstance(context).carDao()
    }
}