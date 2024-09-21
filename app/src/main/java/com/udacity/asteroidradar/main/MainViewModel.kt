package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidRadarApiServices
import com.udacity.asteroidradar.database.getDatabaseInstance
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.utils.FilterAsteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabaseInstance(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _navigateToDetailAsteroid = MutableLiveData<Asteroid?>()
    val navigateToDetailAsteroid: LiveData<Asteroid?>
        get() = _navigateToDetailAsteroid

    private var _filterAsteroid = MutableLiveData(FilterAsteroid.ALL)

    private val allAsteroids: LiveData<PagingData<Asteroid>> =
        asteroidRepository.getAsteroidsPagingData().asLiveData()

    @RequiresApi(Build.VERSION_CODES.O)
    val weeklyAsteroid: LiveData<PagingData<Asteroid>> =
        asteroidRepository.getWeeklyAsteroidsPagingSource().asLiveData()

    @RequiresApi(Build.VERSION_CODES.O)
    val presentDayAsteroid: LiveData<PagingData<Asteroid>> =
        asteroidRepository.getPresentDayAsteroidsPagingSource().asLiveData()

    @RequiresApi(Build.VERSION_CODES.O)
    val asteroidList: LiveData<PagingData<Asteroid>> = _filterAsteroid.switchMap { filter ->
        when (filter!!) {
            FilterAsteroid.WEEK -> weeklyAsteroid
            FilterAsteroid.PRESENT_DAY -> presentDayAsteroid
            else -> allAsteroids
        }
    }

    init {
        viewModelScope.launch {
            asteroidRepository.fetchAsteroids()
            fetchPictureOfDay()
        }
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailAsteroid.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToDetailAsteroid.value = null
    }

    private suspend fun fetchPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                _pictureOfDay.postValue(
                    AsteroidRadarApiServices.AsteroidApi.retrofitService.getPictureOfTheDay(
                        BuildConfig.API_KEY
                    )
                )
            } catch (err: Exception) {
                Log.e("refreshPictureOfDay", err.message ?: "Unknown error")
            }
        }
    }

    fun setFilter(filter: FilterAsteroid) {
        _filterAsteroid.value = filter
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}

