package com.race.mediocreplan.ui

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.race.mediocreplan.R
import com.race.mediocreplan.data.model.Task
import kotlinx.android.synthetic.main.item_section_title.view.*
import android.util.Pair as UtilPair


class MyTaskRecyclerViewAdapter(
        private val mListener: OnListFragmentInteractionListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var tasksInProgress: MutableList<Task> = mutableListOf()
    private var tasksPlanned: MutableList<Task> = mutableListOf()
    private var tasksFinished: MutableList<Task> = mutableListOf()

    fun setItems(tasks: List<Task>?) {
        tasksPlanned.clear()
        tasksInProgress.clear()
        tasksFinished.clear()
        if (tasks is List<Task>) {
            for (task in tasks) {
                if (task.getStatus() == Task.UNPLANNED) continue
                when (task.getStatus()) {
                    Task.PLANNED -> tasksPlanned.add(task)
                    Task.IN_PROGRESS -> tasksInProgress.add(task)
                    Task.FINISHED -> tasksFinished.add(task)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == TYPE_SECTION_TITLE) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_section_title, parent, false)
            SectionTitleViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_task, parent, false)
            val holder = TaskViewHolder(view)
            holder.cardView.setOnClickListener { _ ->
                val task = getTaskFromPosition(holder.adapterPosition)
                mListener.onTaskClick(task, holder)
            }
            holder.buttonStartNow.setOnClickListener { _ ->
                val task = getTaskFromPosition(holder.adapterPosition)
                mListener.onTaskButtonStartNowClick(task)
            }
            holder
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is SectionTitleViewHolder) {
            val vh = holder as SectionTitleViewHolder
            val context = vh.mView.context
            vh.textSectionTitle.text = when (position) {
                getSectionTitlePosition(TITLE_IN_PROGRESS) -> context.getString(R.string.section_title_in_progress)
                getSectionTitlePosition(TITLE_PLANNED) -> context.getString(R.string.section_title_planned)
                getSectionTitlePosition(TITLE_FINISHED) -> context.getString(R.string.section_title_finished)
                else -> ""
            }
        } else {
            val vh = holder as TaskViewHolder
            val item = getTaskFromPosition(position)
            vh.bind(item)
        }
    }

    private fun getTaskFromPosition(position: Int): Task {
        return when (position) {
            in getSectionTitlePosition(TITLE_IN_PROGRESS)..getSectionTitlePosition(TITLE_PLANNED) ->
                tasksInProgress[position - getSectionTitlePosition(TITLE_IN_PROGRESS) - 1]
            in getSectionTitlePosition(TITLE_PLANNED)..getSectionTitlePosition(TITLE_FINISHED) ->
                tasksPlanned[position - getSectionTitlePosition(TITLE_PLANNED) - 1]
            else -> tasksFinished[position - getSectionTitlePosition(TITLE_FINISHED) - 1]
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == getSectionTitlePosition(TITLE_IN_PROGRESS) ||
                position == getSectionTitlePosition(TITLE_PLANNED) ||
                position == getSectionTitlePosition(TITLE_FINISHED))
            TYPE_SECTION_TITLE
        else TYPE_TASK
    }

    private fun getSectionTitlePosition(title: Int): Int {
        return when (title) {
            TITLE_IN_PROGRESS -> 0
            TITLE_PLANNED -> tasksInProgress.size + 1
            TITLE_FINISHED -> tasksInProgress.size + tasksPlanned.size + 2
            else -> RecyclerView.NO_POSITION
        }
    }

    override fun getItemId(position: Int): Long {
        return when (position) {
            getSectionTitlePosition(TITLE_IN_PROGRESS) -> TITLE_IN_PROGRESS.toLong()
            getSectionTitlePosition(TITLE_PLANNED) -> TITLE_PLANNED.toLong()
            getSectionTitlePosition(TITLE_FINISHED) -> TITLE_FINISHED.toLong()
            else -> getTaskFromPosition(position)._id.toLong()
        }
    }

    override fun getItemCount(): Int = tasksInProgress.size + tasksPlanned.size + tasksFinished.size + 3

    inner class SectionTitleViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textSectionTitle: TextView = mView.text_section_title

        override fun toString(): String {
            return super.toString() + " '" + textSectionTitle.text + "'"
        }
    }

    companion object {
        const val TAG = "MyTaskRecyclerViewAdapter"
        const val TYPE_TASK = -1
        const val TYPE_SECTION_TITLE = 0
        const val TITLE_IN_PROGRESS = -7
        const val TITLE_PLANNED = -8
        const val TITLE_FINISHED = -9
    }
}
