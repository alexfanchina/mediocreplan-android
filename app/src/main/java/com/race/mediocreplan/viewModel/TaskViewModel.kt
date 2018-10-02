package com.race.mediocreplan.viewModel

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.race.mediocreplan.data.model.Task
import com.race.mediocreplan.data.repo.TaskRepository

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

    companion object {
        fun create(fragment: Fragment, application: Application): TaskViewModel {
            val taskViewModel = ViewModelProviders.of(fragment).get(TaskViewModel::class.java)
            taskViewModel.mTaskRepository = TaskRepository(application)
            taskViewModel.mAllTasks = taskViewModel.mTaskRepository!!.getAllTasks()
            return taskViewModel
        }
    }
}