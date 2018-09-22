package com.race.mediocreplan.data.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.race.mediocreplan.R

data class Task(@SerializedName("id") val _id: Int) {
    @SerializedName("title")
    val title: String = ""
    @SerializedName("narration")
    val narration: String = ""
    @SerializedName("duration")
    val duration: Period = Period()
    @SerializedName("popularity")
    val popularity: Int = 0
    @SerializedName("cardIdentifier")
    val cardIdentifier: String = ""
    @SerializedName("contributor")
    val contributor: String = ""

    inner class Period(@SerializedName("days") val days: Int = 0,
                       @SerializedName("months") val months: Int = 0,
                       @SerializedName("years") val years: Int = 0) {
        fun toString(context: Context): String {
            when {
                years > 0 -> return context.resources.getQuantityString(R.plurals.year_for_period, years, years)
                months > 0 -> return context.resources.getQuantityString(R.plurals.month_for_period, months, months)
                days % 7 == 0 -> return context.resources.getQuantityString(R.plurals.week_for_period, (days / 7), (days / 7))
                days > 0 -> return context.resources.getQuantityString(R.plurals.day_for_period, days, days)
            }
            return "null"
        }
    }
}