package com.race.mediocreplan.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.race.mediocreplan.data.model.Task
import java.util.*


@Dao
interface TaskDao {

    @Query("SELECT * from task_table ORDER BY title ASC")
    fun getAllTasks(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTasks(vararg tasks: Task)

    @Query("UPDATE task_table SET added = :added WHERE _id = :id")
    fun updateTaskAdded(id: Int, added: Boolean)

    @Query("UPDATE task_table SET added = 1, start_date = :startDate WHERE _id = :id")
    fun updateStartedTask(id: Int, startDate: Date)
}