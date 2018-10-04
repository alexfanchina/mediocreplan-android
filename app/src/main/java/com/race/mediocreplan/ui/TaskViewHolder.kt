package com.race.mediocreplan.ui

import android.content.res.ColorStateList
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.race.mediocreplan.R
import com.race.mediocreplan.data.model.Task
import com.race.mediocreplan.ui.utils.TaskItemUtils
import kotlinx.android.synthetic.main.item_task.view.*

class TaskViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
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

    fun bind(item: Task) {
        val context = mView.context
        textTitle.text = item.title
        textNarration.text = item.narration
        textPeriod.text = item.duration.toString(context)
        textPopularity.text = context.getString(R.string.desc_popularity, item.popularity)
        textContributor.text = context.getString(R.string.desc_contributor, item.contributor)
        cardView.setCardBackgroundColor(ColorStateList.valueOf(
                context.getColor(TaskItemUtils.getCardColor(item.cardIdentifier))))
        val textColor = context.getColor(TaskItemUtils.getTextColor(item.cardIdentifier))
        textTitle.setTextColor(textColor)
        textNarration.setTextColor(textColor)
        textPeriod.setTextColor(textColor)
        textPeriod.compoundDrawableTintList = ColorStateList.valueOf(textColor)
        textPopularity.setTextColor(textColor)
        textPopularity.compoundDrawableTintList = ColorStateList.valueOf(textColor)
        textContributor.setTextColor(textColor)
        textContributor.compoundDrawableTintList = ColorStateList.valueOf(textColor)
        val buttonColor = context.getColor(TaskItemUtils.getButtonColor(item.cardIdentifier))
        buttonStartNow.backgroundTintList = ColorStateList.valueOf(buttonColor)
        val timeUsed = item.getTimeUsed()
        if (timeUsed < 0) {
            buttonStartNow.text = context.getString(R.string.action_start_now)
            buttonStartNow.isEnabled = true
        } else {
            buttonStartNow.text = context.getString(R.string.hint_in_progress)
            buttonStartNow.isEnabled = false
        }
        with(cardView) {
            tag = item
        }
    }
}