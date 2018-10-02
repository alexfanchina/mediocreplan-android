package com.race.mediocreplan.data.model

import android.arch.persistence.room.TypeConverter

class PeriodConverters {
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
}