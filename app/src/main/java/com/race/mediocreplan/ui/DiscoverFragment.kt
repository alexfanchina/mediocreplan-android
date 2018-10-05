package com.race.mediocreplan.ui

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.race.mediocreplan.R
import com.race.mediocreplan.data.repo.TaskRepository
import com.race.mediocreplan.viewModel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_discover.view.*


class DiscoverFragment : Fragment() {

    private var columnCount = 1
    private var taskViewModel: TaskViewModel? = null
    private var taskAdapter: TaskRecyclerViewAdapter? = null
    private var listener: OnListFragmentInteractionListener? = null

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
        view.swipe_refresh.setProgressViewOffset(true,
                resources.getDimensionPixelOffset(R.dimen.swipe_refresh_start_position),
                resources.getDimensionPixelOffset(R.dimen.swipe_refresh_end_position))
        view.swipe_refresh.setColorSchemeColors(view.context.getColor(R.color.colorAccent))
        view.swipe_refresh.setOnRefreshListener {
            taskViewModel?.getAllTasks(observer = object : TaskRepository.TaskRepoObserver {
                override fun onObserve() {
                    view.swipe_refresh.isRefreshing = false
                }
            })
        }
        with(view.list) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            taskAdapter = TaskRecyclerViewAdapter(listener!!)
            taskAdapter?.setHasStableIds(true)
            adapter = taskAdapter
            setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                listener?.onListCanScrollUpChanged((layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() != 0)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnItemClickInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
        const val TAG = "DiscoverFragment"

        @JvmStatic
        fun newInstance(columnCount: Int) =
                DiscoverFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
