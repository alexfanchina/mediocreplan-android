package com.race.mediocreplan.ui

import com.race.mediocreplan.data.model.Task

interface OnListFragmentInteractionListener {
    fun onTaskClick(item: Task?, viewHolder: TaskViewHolder?)
    fun onTaskButtonStartNowClick(item: Task?)
    fun onListCanScrollUpChanged(canScrollDown: Boolean)
}