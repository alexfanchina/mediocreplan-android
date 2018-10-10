package com.race.mediocreplan.ui

import com.race.mediocreplan.data.model.Task

interface OnListFragmentInteractionListener {
    fun onTaskClick(item: Task?, viewHolder: TaskViewHolder?)
    fun onTaskLongClick(item: Task?): Boolean
    fun onTaskButtonStartNowClick(item: Task?)
    fun onListCanScrollUpChanged(canScrollDown: Boolean)
}