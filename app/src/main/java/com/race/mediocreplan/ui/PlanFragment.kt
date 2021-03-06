package com.race.mediocreplan.ui

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.race.mediocreplan.R
import com.race.mediocreplan.viewModel.TaskViewModel


class PlanFragment : Fragment() {

    private var columnCount = 1
    private lateinit var taskViewModel: TaskViewModel
    private var myTaskAdapter: MyTaskRecyclerViewAdapter? = null
    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        taskViewModel = TaskViewModel.create(this, activity!!.application)
        taskViewModel.getAllTasks().observe(this, Observer { tasks ->
            if (myTaskAdapter != null) {
                Log.d(DiscoverFragment.TAG, "all tasks live data updated")
                myTaskAdapter!!.setItems(tasks)
                myTaskAdapter!!.notifyDataSetChanged()
            } else Log.e(TAG, "myTaskAdapter is null")
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_plan, container, false)

        columnCount = resources.getInteger(R.integer.column_number)
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> {
                        StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL)
                    }
                }
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (layoutManager is LinearLayoutManager) {
                            (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0
                        } else listener?.onListCanScrollUpChanged(canScrollVertically(-1))
                    }
                })
                myTaskAdapter = MyTaskRecyclerViewAdapter(listener!!)
                myTaskAdapter?.setHasStableIds(true)
                Log.d(TAG, "myTaskRecyclerViewAdapter initiated")
                adapter = myTaskAdapter
            }
            view.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                listener?.onListCanScrollUpChanged(view.canScrollVertically(-1))
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
        const val TAG = "PlanFragment"
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
                PlanFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
