package com.thisthatdc.calorytracker.util

import java.time.Clock

class Time {
    companion object {
        const val DAY_MILLIS = 1000 * 60 * 60 * 24
        fun getCurrentStartingDayMillis() : Long {
            val clock = Clock.systemUTC().instant()
            val now = clock.toEpochMilli()
            return (now - (now % DAY_MILLIS))
        }

        fun getNextStartingDayMillis() : Long {
            val clock = Clock.systemUTC().instant()
            val now = clock.toEpochMilli()
            return (now - (now % DAY_MILLIS)) + DAY_MILLIS
        }
    }
}