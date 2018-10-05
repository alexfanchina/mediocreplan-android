package com.race.mediocreplan.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import com.race.mediocreplan.R
import java.util.*

@Entity(tableName = "task_table")
data class Task(@PrimaryKey @NonNull @ColumnInfo(name = "_id") @SerializedName("task_id") var _id: Int) : Parcelable {
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
    @ColumnInfo(name = "added")
    var added: Boolean = false
    @ColumnInfo(name = "start_date")
    var startTime: Date? = Date(Long.MAX_VALUE)

    fun getTimeUsed(): Long {
        return if (startTime == null) -1
        else (Date().time - startTime!!.time) / 1000
    }

    fun getTimeExpected(): Long {
        return duration.calcTimeSeconds()
    }

    fun getStatus(): Int {
        val timeUsed = getTimeUsed()
        val timeExpected = getTimeExpected()
        return when {
            !added -> UNPLANNED
            timeUsed < 0 -> PLANNED
            timeUsed in 0..timeExpected -> IN_PROGRESS
            timeUsed >= timeExpected -> FINISHED
            else -> UNPLANNED
        }
    }

    fun getTimeUsedPercentage(): Int {
        return Math.min(100, (getTimeUsed() / (getTimeExpected() * 1.0) * 100).toInt())
    }

    constructor(parcel: Parcel) : this(parcel.readInt()) {
        title = parcel.readString()!!
        narration = parcel.readString()!!
        duration = parcel.readParcelable(Period::class.java.classLoader)!!
        popularity = parcel.readInt()
        cardIdentifier = parcel.readString()!!
        contributor = parcel.readString()!!
        added = parcel.readByte() != 0.toByte()
        startTime = Date(parcel.readLong())
    }

    class Period(@SerializedName("days") var days: Int = 0,
                 @SerializedName("months") var months: Int = 0,
                 @SerializedName("years") var years: Int = 0) : Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt())

        fun calcTimeSeconds(): Long {
            return ((years * 365 + months * 30 + days) * 24 * 60 * 60).toLong()
        }

        fun toString(context: Context): String {
            when {
                years > 0 -> return context.resources.getQuantityString(R.plurals.year_for_period, years, years)
                months > 0 -> return context.resources.getQuantityString(R.plurals.month_for_period, months, months)
                days % 7 == 0 -> return context.resources.getQuantityString(R.plurals.week_for_period, (days / 7), (days / 7))
                days > 0 -> return context.resources.getQuantityString(R.plurals.day_for_period, days, days)
            }
            return "null"
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(days)
            parcel.writeInt(months)
            parcel.writeInt(years)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Period> {
            override fun createFromParcel(parcel: Parcel): Period {
                return Period(parcel)
            }

            override fun newArray(size: Int): Array<Period?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(_id)
        parcel.writeString(title)
        parcel.writeString(narration)
        parcel.writeParcelable(duration, flags)
        parcel.writeInt(popularity)
        parcel.writeString(cardIdentifier)
        parcel.writeString(contributor)
        parcel.writeByte(if (added) 1 else 0)
        parcel.writeLong(if (startTime != null) startTime!!.time else Long.MAX_VALUE)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        const val UNPLANNED = -1
        const val PLANNED = 1
        const val IN_PROGRESS = 2
        const val FINISHED = 3

        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}