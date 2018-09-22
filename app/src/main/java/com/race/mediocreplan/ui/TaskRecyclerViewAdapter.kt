package com.race.mediocreplan.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.race.mediocreplan.R
import com.race.mediocreplan.data.model.Task
import kotlinx.android.synthetic.main.item_task.view.*

/**
 * [RecyclerView.Adapter] that can display a [Task] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class TaskRecyclerViewAdapter()
    : RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>() {

    private var items: List<Task> = ArrayList()
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Task
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
        }
    }

    fun setItems(tasks: List<Task>?) {
        if (tasks is List<Task>)
            items = tasks
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.mView.context
        val item = items[position]
        holder.textTitle.text = item.title
        holder.textNarration.text = item.narration
        holder.textPeriod.text = item.duration.toString(context)
        holder.textPopularity.text = context.getString(R.string.desc_popularity, item.popularity)
        holder.textContributor.text = context.getString(R.string.desc_contributor, item.contributor)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
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
