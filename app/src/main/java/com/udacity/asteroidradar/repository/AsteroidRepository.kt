package com.udacity.asteroidradar.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.api.AsteroidRadarApiServices
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.toDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime

class AsteroidRepository(private val database: AsteroidDatabase) {
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    @RequiresApi(Build.VERSION_CODES.O)
    private val startDate = LocalDateTime.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val endDate = LocalDateTime.now().minusDays(/* days = */ 7)


    fun getAsteroidsPagingData(): Flow<PagingData<Asteroid>> {
        return Pager(PagingConfig(pageSize = 20)) {
            database.asteroidDao.getAsteroidsOrderByCloseApproachDate()
        }.flow.map { pagingData ->
            pagingData.map { it.toDomainModel() }
        }.cachedIn(repositoryScope)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getPresentDayAsteroidsPagingSource(): Flow<PagingData<Asteroid>> {
        return Pager(PagingConfig(pageSize = 20)) {
            database.asteroidDao.getAsteroidsByStartDate(startDate.toString())
        }.flow.map { pagingData ->
            pagingData.map { it.toDomainModel() } // Ensure to map to the domain model
        }.cachedIn(repositoryScope)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeeklyAsteroidsPagingSource(): Flow<PagingData<Asteroid>> {
        return Pager(PagingConfig(pageSize = 20)) {
            database.asteroidDao.getAsteroidsByStartAndEndDate(
                startDate.toString(),
                endDate.toString()
            )
        }.flow.map { pagingData ->
            pagingData.map { it.toDomainModel() }
        }.cachedIn(repositoryScope)
    }

    suspend fun fetchAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val apiKey = BuildConfig.API_KEY
                val asteroids =
                    AsteroidRadarApiServices.AsteroidApi.retrofitService.getAsteroids(apiKey)
                val result = parseAsteroidsJsonResult(JSONObject(asteroids))
                database.asteroidDao.insertAll(*result.asDatabaseModel())
                Log.d("fetched Asteroids", "Success")
            } catch (error: Exception) {
                Log.e("Failed:", error.message.toString())
            }
        }
    }

}