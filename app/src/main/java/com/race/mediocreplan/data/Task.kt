package com.race.mediocreplan.data

import android.content.Context
import com.race.mediocreplan.R

class Task(val _id: Int) {
    val title: String = ""
    val narration: String = ""
    val duration: Period = Period()
    val popularity: Int = 0
    val cardIdentifier: String = ""
    val contributor: String = ""

    inner class Period(val days: Int = 0, val months: Int = 0, val years: Int = 0) {
        fun toString(context: Context): String {
            if (years > 0) return context.resources.getQuantityString(R.plurals.year_for_period, years)
            else if (months > 0) return context.resources.getQuantityString(R.plurals.month_for_period, months)
            else if (days % 7 == 0) return context.resources.getQuantityString(R.plurals.week_for_period, (days / 7))
            else if (days > 0) return context.resources.getQuantityString(R.plurals.day_for_period, days)
            return "null"
        }
    }
}