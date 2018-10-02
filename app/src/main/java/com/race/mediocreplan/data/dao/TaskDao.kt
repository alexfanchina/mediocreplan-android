package com.race.mediocreplan.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.race.mediocreplan.data.model.Task


@Dao
interface TaskDao {

    @Query("SELECT * from task_table ORDER BY title ASC")
    fun getAllTasks(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTasks(vararg tasks: Task)
}