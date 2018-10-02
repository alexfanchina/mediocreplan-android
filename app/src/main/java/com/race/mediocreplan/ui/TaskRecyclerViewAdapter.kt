package com.race.mediocreplan.ui

import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.race.mediocreplan.R
import com.race.mediocreplan.data.model.Task
import com.race.mediocreplan.ui.utils.TaskItemUtils
import kotlinx.android.synthetic.main.item_task.view.*

class TaskRecyclerViewAdapter(
        private val recyclerView: RecyclerView,
        private val mListener: OnItemClickInteractionListener?)
    : RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>() {

    private var items: List<Task> = ArrayList()
    private var mExpandedPosition: Int = -1

    fun setItems(tasks: List<Task>?) {
        if (tasks is List<Task>)
            items = tasks
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false)
        val holder = ViewHolder(view)
        holder.cardView.setOnClickListener { v ->
            val item = v.tag as Task
            val prevExpandedPosition = mExpandedPosition
            mExpandedPosition = if (holder.adapterPosition == mExpandedPosition)
                RecyclerView.NO_POSITION else holder.adapterPosition
            mListener?.onItemClickInteraction(item)
            // TODO: fix the transition
            val transition = AutoTransition()
            transition.duration = 200
            TransitionManager.beginDelayedTransition(holder.mView as ViewGroup, transition)
//            TransitionManager.beginDelayedTransition(recyclerView)
            notifyDataSetChanged()
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isExpanded: Boolean = (position == mExpandedPosition)
        val context = holder.mView.context
        val item = items[position]
        holder.textTitle.text = item.title
        holder.textNarration.text = item.narration
        holder.textPeriod.text = item.duration.toString(context)
        holder.textPopularity.text = context.getString(R.string.desc_popularity, item.popularity)
        holder.textContributor.text = context.getString(R.string.desc_contributor, item.contributor)
        holder.textNarration.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.itemView.isActivated = isExpanded
        holder.cardView.setCardBackgroundColor(ColorStateList.valueOf(
                context.getColor(TaskItemUtils.getCardColor(item.cardIdentifier))))
        val textColor = context.getColor(TaskItemUtils.getTextColor(item.cardIdentifier))
        holder.textTitle.setTextColor(textColor)
        holder.textNarration.setTextColor(textColor)
        holder.textPeriod.setTextColor(textColor)
        holder.textPeriod.compoundDrawableTintList = ColorStateList.valueOf(textColor)
        holder.textPopularity.setTextColor(textColor)
        holder.textPopularity.compoundDrawableTintList = ColorStateList.valueOf(textColor)
        holder.textContributor.setTextColor(textColor)
        holder.textContributor.compoundDrawableTintList = ColorStateList.valueOf(textColor)
        with(holder.cardView) {
            tag = item
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int = items.size

    interface OnItemClickInteractionListener {
        fun onItemClickInteraction(item: Task)
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val cardView: CardView = mView.card
        val textTitle: TextView = mView.text_title
        val textNarration: TextView = mView.text_narration
        val textPeriod: TextView = mView.text_period
        val textPopularity: TextView = mView.text_popularity
        val textContributor: TextView = mView.text_contributor

        override fun toString(): String {
            return super.toString() + " '" + textTitle.text + "'"
        }
    }
}
