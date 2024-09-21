package com.udacity.asteroidradar

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
//data class Asteroid(val id: Long, val codename: String, val closeApproachDate: String,
//                    val absoluteMagnitude: Double, val estimatedDiameter: Double,
//                    val relativeVelocity: Double, val distanceFromEarth: Double,
//                    val isPotentiallyHazardous: Boolean) : Parcelable


data class Asteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
/*    val estimatedDiameterMax: Double,*/
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean,
//    val missDistance: Double,
    val isSentryObject: Boolean
) : Parcelable
