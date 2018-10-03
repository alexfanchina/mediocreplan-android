package com.race.mediocreplan.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.race.mediocreplan.R
import com.race.mediocreplan.data.model.Task
import com.race.mediocreplan.viewModel.TaskViewModel


class DiscoverFragment : Fragment() {

    private var taskViewModel: TaskViewModel? = null

    // TODO: Customize parameters
    private var columnCount = 1

    private var taskAdapter: TaskRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        taskViewModel = TaskViewModel.create(this, activity!!.application)
        taskViewModel!!.getAllTasks().observe(this, Observer { tasks ->
            Log.d(TAG, "all tasks live data updated")
            if (taskAdapter != null) {
                taskAdapter!!.setItems(tasks)
                taskAdapter!!.notifyDataSetChanged()
            } else {
                Log.e(TAG, "taskAdapter is null")
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_discover, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                taskAdapter = TaskRecyclerViewAdapter(
                        view,
                        object : TaskRecyclerViewAdapter.OnItemClickInteractionListener {
                            override fun onItemClick(item: Task) {
                                Log.d(TAG, "$item clicked " + Gson().toJson(item))
                                TaskDetailActivity.actionStart(context, item)
                            }

                            override fun onItemButtonStartNowClicked(item: Task) {
                                taskViewModel!!.startTask(item)
                            }
                        })
                taskAdapter?.setHasStableIds(true)
                adapter = taskAdapter
                // TODO: Read items from local data
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
        const val TAG = "DiscoverFragment"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                DiscoverFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
