package com.race.mediocreplan.ui

import android.content.res.ColorStateList
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.race.mediocreplan.R
import com.race.mediocreplan.data.model.Task
import com.race.mediocreplan.ui.PlanFragment.OnListFragmentInteractionListener
import com.race.mediocreplan.ui.utils.TaskItemUtils
import kotlinx.android.synthetic.main.item_section_title.view.*
import kotlinx.android.synthetic.main.item_task.view.*


class MyTaskRecyclerViewAdapter(
        private val mListener: OnListFragmentInteractionListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
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

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Task
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener.onTaskClick(item)
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
                mListener.onTaskClick(task)
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
            val context = vh.mView.context
            vh.textTitle.text = item.title
            vh.textNarration.text = item.narration
            vh.textPeriod.text = item.duration.toString(context)
            vh.textPopularity.text = context.getString(R.string.desc_popularity, item.popularity)
            vh.textContributor.text = context.getString(R.string.desc_contributor, item.contributor)
            vh.cardView.setCardBackgroundColor(ColorStateList.valueOf(
                    context.getColor(TaskItemUtils.getCardColor(item.cardIdentifier))))
            val textColor = context.getColor(TaskItemUtils.getTextColor(item.cardIdentifier))
            vh.textTitle.setTextColor(textColor)
            vh.textNarration.setTextColor(textColor)
            vh.textPeriod.setTextColor(textColor)
            vh.textPeriod.compoundDrawableTintList = ColorStateList.valueOf(textColor)
            vh.textPopularity.setTextColor(textColor)
            vh.textPopularity.compoundDrawableTintList = ColorStateList.valueOf(textColor)
            vh.textContributor.setTextColor(textColor)
            vh.textContributor.compoundDrawableTintList = ColorStateList.valueOf(textColor)
            val buttonColor = context.getColor(TaskItemUtils.getButtonColor(item.cardIdentifier))
            vh.buttonStartNow.backgroundTintList = ColorStateList.valueOf(buttonColor)
            val timeUsed = item.getTimeUsed()
            if (timeUsed < 0) {
                vh.buttonStartNow.text = context.getString(R.string.action_start_now)
                vh.buttonStartNow.isEnabled = true
            } else {
                vh.buttonStartNow.text = context.getString(R.string.hint_in_progress)
                vh.buttonStartNow.isEnabled = false
            }
            with(vh.cardView) {
                tag = item
            }
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

    override fun getItemCount(): Int = tasksInProgress.size + tasksPlanned.size + tasksFinished.size + 3

    inner class TaskViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val cardView: CardView = mView.card
        val textTitle: TextView = mView.text_title
        val textNarration: TextView = mView.text_narration
        val textPeriod: TextView = mView.text_period
        val textPopularity: TextView = mView.text_popularity
        val textContributor: TextView = mView.text_contributor
        val buttonStartNow: Button = mView.button_start_now

        override fun toString(): String {
            return super.toString() + " '" + textTitle.text + "'"
        }
    }

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
        const val TITLE_IN_PROGRESS = 2
        const val TITLE_PLANNED = 1
        const val TITLE_FINISHED = 3
    }
}
