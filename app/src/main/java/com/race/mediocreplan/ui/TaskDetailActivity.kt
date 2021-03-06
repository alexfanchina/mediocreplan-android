package com.race.mediocreplan.ui

import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.*
import android.util.Log
import android.view.View
import com.google.gson.Gson
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
        background.setOnClickListener { onBackPressed() }
        initStyle()
        loadData()
    }

    override fun onBackPressed() {
        val transition = AutoTransition()
        transition.duration = 200
        TransitionManager.beginDelayedTransition(card, transition)
        text_narration.visibility = View.GONE
        button_start_now.visibility = View.GONE
        button_add_to_plan.visibility = View.GONE
        supportFinishAfterTransition()
    }

    private fun initStyle() {
        card.setCardBackgroundColor(ColorStateList.valueOf(
                getColor(TaskItemUtils.getCardColor(task.cardIdentifier))))
        val textColor = getColor(TaskItemUtils.getTextColor(task.cardIdentifier))
        val textColorTintList = ColorStateList.valueOf(textColor)
        text_title.setTextColor(textColor)
        text_narration.setTextColor(textColor)
        text_period.setTextColor(textColor)
        text_popularity.setTextColor(textColor)
        text_contributor.setTextColor(textColor)
        text_progress_done.setTextColor(textColor)
        text_progress_left.setTextColor(textColor)
        text_period.compoundDrawableTintList = textColorTintList
        text_popularity.compoundDrawableTintList = textColorTintList
        text_contributor.compoundDrawableTintList = textColorTintList
        progress.progressBackgroundTintList = textColorTintList
        progress.progressTintList = textColorTintList
        val buttonColor = getColor(TaskItemUtils.getButtonColor(task.cardIdentifier))
        button_start_now.backgroundTintList = ColorStateList.valueOf(buttonColor)
        button_add_to_plan.backgroundTintList = ColorStateList.valueOf(buttonColor)

        text_narration.visibility = View.GONE
        button_start_now.visibility = View.GONE
        button_add_to_plan.visibility = View.GONE
        window.sharedElementEnterTransition.duration = 200
        window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                val t = AutoTransition()
                t.duration = 200
                TransitionManager.beginDelayedTransition(card, t)
                text_narration.visibility = View.VISIBLE
                button_start_now.visibility = View.VISIBLE
                button_add_to_plan.visibility = View.VISIBLE
            }

            override fun onTransitionResume(transition: Transition?) {}
            override fun onTransitionPause(transition: Transition?) {}
            override fun onTransitionCancel(transition: Transition?) {}
            override fun onTransitionStart(transition: Transition?) {}
        })
    }

    private fun loadData() {
        Log.d(TAG, "load " + Gson().toJson(task))
        Log.d(TAG, "${task._id}: startDate=${task.startTime}, timeUsed=${task.getTimeUsed()}, timeExpected=${task.getTimeExpected()}")
        text_title.text = task.title
        text_narration.text = task.narration
        text_period.text = task.duration.toString(this)
        text_popularity.text = getString(R.string.desc_popularity, task.popularity)
        text_contributor.text = getString(R.string.desc_contributor, task.contributor)
        button_start_now.setOnClickListener { v ->
            taskViewModel.startTask(task)
            loadData()
        }
        TransitionManager.beginDelayedTransition(card)
        when (task.getStatus()) {
            Task.UNPLANNED -> {
                button_add_to_plan.text = getString(R.string.action_add_to_plan)
                button_add_to_plan.setOnClickListener { _ ->
                    taskViewModel.addTaskToPlan(task)
                    loadData()
                }
                progress_layout.visibility = View.GONE
            }
            Task.PLANNED -> {
                button_add_to_plan.text = getString(R.string.action_remove_from_plan)
                button_add_to_plan.setOnClickListener { _ ->
                    taskViewModel.removeTaskFromPlan(task)
                    loadData()
                }
                progress_layout.visibility = View.GONE
            }
            Task.IN_PROGRESS, Task.FINISHED -> {
                button_start_now.text = getString(R.string.action_restart)
                button_add_to_plan.text = getString(R.string.action_abolish)
                button_add_to_plan.setOnClickListener { _ ->
                    taskViewModel.abolishTask(task)
                    loadData()
                }
                progress_layout.visibility = View.VISIBLE
                val percentage = task.getTimeUsedPercentage()
                val progressAnimator = ObjectAnimator.ofInt(progress, "progress", progress.progress, percentage)
                progressAnimator.duration = 200
                progressAnimator.start()
                text_progress_done.text = String.format(getString(R.string.period_done), percentage)
                text_progress_left.text = if (task.getStatus() == Task.IN_PROGRESS)
                    String.format(getString(R.string.period_left), "${(task.getTimeExpected() - task.getTimeUsed()) / 24 / 3600} days")
                else getString(R.string.period_left_finished)
            }
        }
    }

    companion object {
        const val TAG = "TaskDetailActivity"
        const val EXTRA_TASK = "extra_task"

        fun actionStart(context: Context, task: Task) {
            val intent = Intent(context, TaskDetailActivity::class.java)
            intent.putExtra(EXTRA_TASK, task)
            context.startActivity(intent)
        }

        fun actionStart(context: Context, task: Task, options: ActivityOptions) {
            val intent = Intent(context, TaskDetailActivity::class.java)
            intent.putExtra(EXTRA_TASK, task)
            context.startActivity(intent, options.toBundle())
        }
    }
}
