package com.udacity.asteroidradar.worker

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

import com.udacity.asteroidradar.database.getDatabaseInstance
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException
import java.time.LocalDate

class AsteroidDataSyncWorker(applicationContext: Context, params: WorkerParameters):CoroutineWorker(applicationContext, params) {

    companion object {
        const val WORK_NAME = "AsteroidDataSyncWorker"
    }
    override suspend fun doWork(): Result {
        val database = getDatabaseInstance(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.fetchAsteroids()
            Result.success()

        } catch (e: HttpException) {
           Result.retry()
        }
    }
}
