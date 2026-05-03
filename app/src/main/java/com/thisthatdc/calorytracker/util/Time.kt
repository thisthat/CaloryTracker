package com.thisthatdc.calorytracker.util

import java.time.Clock
import java.util.Date

class Time {
    companion object {
        const val DAY_MILLIS = 1000 * 60 * 60 * 24
        fun getCurrentStartingDayMillis(): Long {
            val clock = Clock.systemUTC().instant()
            val now = clock.toEpochMilli()
            return (now - (now % DAY_MILLIS))
        }

        fun getNextStartingDayMillis(): Long {
            val clock = Clock.systemUTC().instant()
            val now = clock.toEpochMilli()
            return (now - (now % DAY_MILLIS)) + DAY_MILLIS
        }

        fun getStartingDayMillis(day: Date): Long {
            val now = day.toInstant().toEpochMilli()
            return (now - (now % DAY_MILLIS))
        }

        fun getNextStartingDayMillis(day: Date): Long {
            val now = day.toInstant().toEpochMilli()
            return (now - (now % DAY_MILLIS)) + DAY_MILLIS
        }

        fun isToday(day: Date): Boolean {
            val now = day.toInstant().toEpochMilli()
            return now > getCurrentStartingDayMillis() && now < getNextStartingDayMillis();
        }
    }
}