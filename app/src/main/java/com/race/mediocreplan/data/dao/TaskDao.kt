package com.race.mediocreplan.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.race.mediocreplan.data.model.Task
import java.util.*
import android.arch.persistence.room.Transaction
import com.race.mediocreplan.data.model.Converter


@Dao
interface TaskDao {

    @Query("SELECT * from task_table ORDER BY title ASC")
    fun getAllTasks(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTasks(vararg tasks: Task)

    @Query("UPDATE task_table SET added = :added WHERE _id = :id")
    fun updateTaskAdded(id: Int, added: Boolean)

    @Query("UPDATE task_table SET added = 1, start_date = :startDate WHERE _id = :id")
    fun updateStartedTask(id: Int, startDate: Date)

    @Query("UPDATE task_table " +
            "SET title = :title, narration = :narration, duration = :duration, popularity = :popularity, " +
            "cardIdentifier = :cardIdentifier, contributor = :contributor " +
            "WHERE _id = :id")
    fun updateTaskFields(id: Int, title: String, narration: String, duration: Int, popularity: Int, cardIdentifier: String, contributor: String)

    @Transaction
    fun insertOrReplaceTasks(vararg tasks: Task) {
        insertTasks(*tasks)
        val converter = Converter()
        for (task in tasks) {
            updateTaskFields(task._id, task.title, task.narration,
                    converter.periodToPeriodInt(task.duration), task.popularity,
                    task.cardIdentifier, task.contributor)
        }
    }
}