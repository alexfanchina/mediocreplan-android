package com.race.mediocreplan.data.repo

import android.app.Application
import android.arch.lifecycle.LiveData
import com.race.mediocreplan.data.dao.TaskDao
import com.race.mediocreplan.data.dao.TaskDatabase
import com.race.mediocreplan.data.model.Task
import android.os.AsyncTask
import android.util.Log
import com.race.mediocreplan.data.RetrofitClientInstance
import com.race.mediocreplan.data.api.MediocrePlanService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository {
    private var mTaskDao: TaskDao? = null
    private var mAllTasks: LiveData<List<Task>>? = null

    constructor(application: Application) {
        val db = TaskDatabase.getDatabase(application)
        mTaskDao = db!!.taskDao()
        mAllTasks = mTaskDao!!.getAllTasks()
    }

    fun getAllTasks(): LiveData<List<Task>> {
        val mediocrePlanService =
                RetrofitClientInstance.getRetrofitInstance()
                        .create(MediocrePlanService::class.java)
        val call = mediocrePlanService.getAllTasks()
        call.enqueue(object : Callback<List<Task>> {
            override fun onFailure(call: Call<List<Task>>?, t: Throwable?) {
                Log.e(TAG, "error" + t?.message)
            }

            override fun onResponse(call: Call<List<Task>>?, response: Response<List<Task>>?) {
                Log.d(TAG, response?.raw().toString() + ": " + response?.body())
                batchInsert(response?.body())
            }
        })
        return mAllTasks!!
    }

    fun insert(task: Task?) {
        if (mTaskDao != null) {
            InsertAsyncTask(mTaskDao!!).execute(task)
            Log.d(TAG, "Insert " + task.toString())
        } else Log.e(TAG, "Insert while mTaskDao is null")
    }

    fun batchInsert(tasks: List<Task>?) {
        if (mTaskDao != null) {
            BatchInsertAsyncTask(mTaskDao!!).execute(tasks)
            Log.d(TAG, "Batch Insert " + tasks.toString())
        } else Log.e(TAG, "Batch Insert while mTaskDao is null")
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: TaskDao) : AsyncTask<Task, Void, Void>() {
        override fun doInBackground(vararg params: Task?): Void? {
            if (params[0] is Task) mAsyncTaskDao.insertTask(params[0]!!)
            else Log.e("InsertAsyncTask", "Insert param cannot be null")
            return null
        }
    }

    private class BatchInsertAsyncTask internal constructor(private val mAsyncTaskDao: TaskDao) : AsyncTask<List<Task>, Void, Void>() {
        override fun doInBackground(vararg params: List<Task>?): Void? {
            if (params[0] != null) mAsyncTaskDao.insertTasks(*params[0]!!.toTypedArray())
            else Log.e("BatchInsertAsyncTask", "Batch insert param cannot be null")
            return null
        }
    }

    companion object {
        val TAG = "TaskRepository"
    }
}