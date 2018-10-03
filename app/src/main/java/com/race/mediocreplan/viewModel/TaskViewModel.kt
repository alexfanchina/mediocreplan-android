package com.race.mediocreplan.viewModel

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.race.mediocreplan.data.model.Task
import com.race.mediocreplan.data.repo.TaskRepository
import java.util.*

class TaskViewModel : ViewModel() {

    private var mTaskRepository: TaskRepository? = null
    private var mAllTasks: LiveData<List<Task>>? = null

    fun getAllTasks(): LiveData<List<Task>> {
        mAllTasks = mTaskRepository!!.getAllTasks()
        return mAllTasks!!
    }

    fun insertTask(task: Task) {
        mTaskRepository!!.insert(task)
    }

    fun batchInsertTask(tasks: List<Task>) {
        mTaskRepository!!.batchInsert(tasks)
    }

    fun addTaskToPlan(task: Task) {
        task.added = true
        mTaskRepository!!.updateAdded(task)
    }

    fun removeTaskFromPlan(task: Task) {
        task.added = false
        mTaskRepository!!.updateAdded(task)
    }

    fun startTask(task: Task) {
        task.added = true
        task.startTime = Date()
        mTaskRepository!!.updateStarted(task)
    }

    fun abolishTask(task: Task) {
        task.startTime = Date(Long.MAX_VALUE)
        mTaskRepository!!.updateStarted(task)
    }

    companion object {
        fun create(fragment: Fragment, application: Application): TaskViewModel {
            val taskViewModel = ViewModelProviders.of(fragment).get(TaskViewModel::class.java)
            taskViewModel.mTaskRepository = TaskRepository(application)
            return taskViewModel
        }

        fun create(activity: Activity, application: Application): TaskViewModel {
            val taskViewModel = ViewModelProviders.of(activity as FragmentActivity)
                    .get(TaskViewModel::class.java)
            taskViewModel.mTaskRepository = TaskRepository(application)
            return taskViewModel
        }
    }
}