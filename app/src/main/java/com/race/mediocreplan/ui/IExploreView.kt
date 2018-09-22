package com.race.mediocreplan.ui

import com.race.mediocreplan.data.model.Task

interface IExploreView {
    fun onTasksLoad(tasks: List<Task>?)
}