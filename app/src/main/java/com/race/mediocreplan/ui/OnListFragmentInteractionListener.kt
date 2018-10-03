package com.race.mediocreplan.ui

import com.race.mediocreplan.data.model.Task

interface OnListFragmentInteractionListener {
    fun onTaskClick(item: Task?)
    fun onTaskButtonStartNowClick(item: Task?)
}