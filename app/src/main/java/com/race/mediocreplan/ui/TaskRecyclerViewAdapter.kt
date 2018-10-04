package com.race.mediocreplan.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.race.mediocreplan.R
import com.race.mediocreplan.data.model.Task

class TaskRecyclerViewAdapter(private val mListener: OnListFragmentInteractionListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Task> = ArrayList()
//    private var mExpandedPosition: Int = -1

    fun setItems(tasks: List<Task>?) {
        if (tasks is List<Task>)
            items = tasks
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false)
        val holder = TaskViewHolder(view)
        holder.cardView.setOnClickListener { v ->
            val item = v.tag as Task
//            val prevExpandedPosition = mExpandedPosition
//            mExpandedPosition = if (holder.adapterPosition == mExpandedPosition)
//                RecyclerView.NO_POSITION else holder.adapterPosition
            mListener.onTaskClick(item)
            // TODO: fix the transition
//            val transition = AutoTransition()
//            transition.duration = 200
//            TransitionManager.beginDelayedTransition(holder.mView as ViewGroup, transition)
//            TransitionManager.beginDelayedTransition(recyclerView)
//            notifyDataSetChanged()
        }
        holder.buttonStartNow.setOnClickListener { _ ->
            val item = items[holder.adapterPosition]
            mListener.onTaskButtonStartNowClick(item)
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items.get(position)
        if (holder is TaskViewHolder) {
            val vh = holder as TaskViewHolder
            vh.bind(item)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int = items.size
}
