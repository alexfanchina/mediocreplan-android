package com.race.mediocreplan.ui

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.race.mediocreplan.R
import com.race.mediocreplan.data.model.Task
import com.race.mediocreplan.viewModel.TaskViewModel


class PlanFragment : Fragment() {

    private lateinit var taskViewModel: TaskViewModel
    private var myTaskAdapter: MyTaskRecyclerViewAdapter? = null

    // TODO: Customize parameters
    private var columnCount = 1

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

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                myTaskAdapter = MyTaskRecyclerViewAdapter(listener!!)
                Log.d(TAG, "myTaskRecyclerViewAdapter initiated")
                adapter = myTaskAdapter
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onTaskClick(item: Task?)

        fun onTaskButtonStartNowClick(item: Task?)
    }

    companion object {

        const val TAG = "PlanFragment"

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                PlanFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
