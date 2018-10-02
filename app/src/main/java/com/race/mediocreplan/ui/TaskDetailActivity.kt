package com.race.mediocreplan.ui

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.race.mediocreplan.R
import com.race.mediocreplan.data.model.Task
import com.race.mediocreplan.ui.utils.TaskItemUtils
import com.race.mediocreplan.viewModel.TaskViewModel
import kotlinx.android.synthetic.main.activity_task_detail.*

class TaskDetailActivity : AppCompatActivity() {

    lateinit var task: Task
    lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        taskViewModel = TaskViewModel.create(this, application)
        task = intent.getParcelableExtra(EXTRA_TASK)
        Log.d(TAG, "get parcelable extra " + task.toString())

        text_title.text = task.title
        text_narration.text = task.narration
        text_period.text = task.duration.toString(this)
        text_popularity.text = getString(R.string.desc_popularity, task.popularity)
        text_contributor.text = getString(R.string.desc_contributor, task.contributor)
        card.setCardBackgroundColor(ColorStateList.valueOf(
                getColor(TaskItemUtils.getCardColor(task.cardIdentifier))))
        val textColor = getColor(TaskItemUtils.getTextColor(task.cardIdentifier))
        text_title.setTextColor(textColor)
        text_narration.setTextColor(textColor)
        text_period.setTextColor(textColor)
        text_period.compoundDrawableTintList = ColorStateList.valueOf(textColor)
        text_popularity.setTextColor(textColor)
        text_popularity.compoundDrawableTintList = ColorStateList.valueOf(textColor)
        text_contributor.setTextColor(textColor)
        text_contributor.compoundDrawableTintList = ColorStateList.valueOf(textColor)
        button_start_now.setOnClickListener { v -> taskViewModel.startTask(task) }
        button_add_to_plan.setOnClickListener { v -> taskViewModel.addTaskToPlan(task) }
    }

    companion object {
        const val TAG = "TaskDetailActivity"
        const val EXTRA_TASK = "extra_task"

        fun actionStart(context: Context, task: Task) {
            val intent = Intent(context, TaskDetailActivity::class.java)
            intent.putExtra(EXTRA_TASK, task)
            context.startActivity(intent)
        }
    }
}
