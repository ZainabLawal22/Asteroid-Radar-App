package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: AsteroidEntity)

 /* @Query ("SELECT * FROM asteroids ORDER BY closeApproachDate DESC")
    fun  getAsteroidsOrderByCloseApproachDate(): LiveData<List<AsteroidEntity>>*/

    @Query("SELECT * FROM asteroids ORDER BY closeApproachDate DESC")
    fun getAsteroidsOrderByCloseApproachDate(): PagingSource<Int, AsteroidEntity>


    @Query("SELECT * FROM asteroids WHERE closeApproachDate = :startDate ORDER BY closeApproachDate DESC")
    fun getAsteroidsByStartDate(startDate: String): PagingSource<Int, AsteroidEntity>

    @Query("SELECT * FROM asteroids WHERE closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate DESC")
    fun getAsteroidsByStartAndEndDate(startDate: String, endDate: String): PagingSource<Int, AsteroidEntity>

}