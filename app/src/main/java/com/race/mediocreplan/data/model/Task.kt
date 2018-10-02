package com.race.mediocreplan.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import android.content.Context
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import com.race.mediocreplan.R

@Entity(tableName = "task_table")
data class Task(@PrimaryKey @NonNull @ColumnInfo(name = "_id") @SerializedName("task_id") var _id: Int) {
    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String = ""
    @ColumnInfo(name = "narration")
    @SerializedName("narration")
    var narration: String = ""
    @ColumnInfo(name = "duration")
    @SerializedName("duration")
    var duration: Period = Period()
    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    var popularity: Int = 0
    @ColumnInfo(name = "cardIdentifier")
    @SerializedName("card_identifier")
    var cardIdentifier: String = ""
    @ColumnInfo(name = "contributor")
    @SerializedName("contributor")
    var contributor: String = ""

    class Period(@SerializedName("days") var days: Int = 0,
                 @SerializedName("months") var months: Int = 0,
                 @SerializedName("years") var years: Int = 0) {
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