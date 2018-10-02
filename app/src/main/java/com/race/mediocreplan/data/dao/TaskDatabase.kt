package com.race.mediocreplan.data.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.arch.persistence.room.Room
import android.arch.persistence.room.TypeConverters
import com.race.mediocreplan.data.model.PeriodConverters
import com.race.mediocreplan.data.model.Task


@Database(entities = [Task::class], version = 1)
@TypeConverters(PeriodConverters::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase? {
            if (INSTANCE == null) {
                synchronized(TaskDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                TaskDatabase::class.java, "mediocre_plan_db")
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }

}