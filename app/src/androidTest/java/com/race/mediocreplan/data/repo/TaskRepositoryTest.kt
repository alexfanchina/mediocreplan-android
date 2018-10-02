package com.race.mediocreplan.data.repo

import android.app.Application
import android.support.test.InstrumentationRegistry
import org.junit.Test


class TaskRepositoryTest {

    val appContext = InstrumentationRegistry.getTargetContext()

    @Test
    fun getAllTasks() {
    }

    @Test
    fun insert() {
    }

    @Test
    fun batchInsert() {
    }

    @Test
    fun updateAdded() {
        val repo = TaskRepository(appContext.applicationContext as Application)
        var task = repo.getAllTasks().value!![0]
        task.added = true
        repo.updateAdded(task)
        task = repo.getAllTasks().value!![0]
        assert(task.added)
    }

    @Test
    fun updateStarted() {
    }
}