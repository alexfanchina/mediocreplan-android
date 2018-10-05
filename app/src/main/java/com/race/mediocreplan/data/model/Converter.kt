package com.race.mediocreplan.data.model

import android.arch.persistence.room.TypeConverter
import java.util.*


class Converter {
    @TypeConverter
    fun fromPeriodInt(value: Int): Task.Period {
        val years = value / 365
        val months = (value - years * 365) / 12
        val days = value - years * 365 - months * 12
        return Task.Period(days, months, years)
    }

    @TypeConverter
    fun periodToPeriodInt(period: Task.Period): Int {
        return period.years * 365 + period.months * 30 + period.days
    }

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun toLong(date: Date?): Long? {
        return if (date == null) null else date.time
    }
}