package com.race.mediocreplan.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.race.mediocreplan.R
import com.race.mediocreplan.data.model.Task
import com.race.mediocreplan.viewModel.TaskViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PlanFragment.OnListFragmentInteractionListener {

    private val fragmentMap: HashMap<Int, Fragment> = HashMap()
    private var currentFragment: Int? = null
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO: UI bug when rotating screen
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            switchTab(menuItem.itemId)
        }
        initTabs()
        taskViewModel = TaskViewModel.create(this, application)
    }

    private fun initTabs() {
        val discoverFragment = DiscoverFragment()
        val planFragment = PlanFragment()
        val contributeFragment = ContributeFragment()
        fragmentMap[R.id.menu_navi_discover] = discoverFragment
        fragmentMap[R.id.menu_navi_plan] = planFragment
        fragmentMap[R.id.menu_navi_contribute] = contributeFragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, discoverFragment)
        fragmentTransaction.add(R.id.fragment_container, planFragment)
        fragmentTransaction.add(R.id.fragment_container, contributeFragment)
        fragmentTransaction.show(discoverFragment)
        fragmentTransaction.hide(planFragment)
        fragmentTransaction.hide(contributeFragment)
        fragmentTransaction.commit()
        action_bar.title = getString(R.string.navi_discover)
    }

    private fun switchTab(itemId: Int): Boolean {
        if (currentFragment == itemId) return false
        val fragment: Fragment?
        if (fragmentMap[itemId] != null) {
            fragment = fragmentMap[itemId]
            Log.d(TAG, "select fragment " + fragment.toString())
        } else {
            fragment = when (itemId) {
                R.id.menu_navi_discover -> DiscoverFragment()
                R.id.menu_navi_plan -> PlanFragment()
                R.id.menu_navi_contribute -> ContributeFragment()
                else -> return false
            }

            Log.d(TAG, "construct and select new fragment " + fragment.toString())
            fragmentMap[itemId] = fragment
        }
        return if (fragment != null) {
            action_bar.title = getString(when (itemId) {
                R.id.menu_navi_discover -> R.string.navi_discover
                R.id.menu_navi_plan -> R.string.navi_plan
                R.id.menu_navi_contribute -> R.string.navi_contribute
                else -> R.string.app_name
            })
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            for (f in fragmentMap.values) {
                if (f == fragment)
                    fragmentTransaction.show(fragment)
                else fragmentTransaction.hide(f)
            }
            fragmentTransaction.commit()
            currentFragment = itemId
            true
        } else false
    }

    override fun onTaskClick(item: Task?) {
        if (item is Task)
            TaskDetailActivity.actionStart(this, item)
    }

    override fun onTaskButtonStartNowClick(item: Task?) {
        if (item is Task)
            taskViewModel.startTask(item)
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
