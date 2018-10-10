package com.race.mediocreplan.ui

import android.app.ActivityOptions
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.race.mediocreplan.R
import com.race.mediocreplan.data.model.Task
import com.race.mediocreplan.viewModel.TaskViewModel
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Pair as UtilPair


class MainActivity : AppCompatActivity(), OnListFragmentInteractionListener {

    private var currentFragment: String? = null
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            switchTab(menuItem.itemId)
        }
        if (savedInstanceState == null) {
            initTabs()
        } else {
            val f = savedInstanceState.getString(KEY_CURRENT_FRAGMENT)
            if (f != null) {
                Log.d(TAG, "restore saved $KEY_CURRENT_FRAGMENT -> $f")
                switchTab(f)
            } else {
                Log.e(TAG, "get null for $KEY_CURRENT_FRAGMENT")
            }
        }
        taskViewModel = TaskViewModel.create(this, application)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(KEY_CURRENT_FRAGMENT, currentFragment)
        Log.d(TAG, "save $KEY_CURRENT_FRAGMENT")
    }

    private fun initTabs() {
        val discoverFragment = DiscoverFragment()
        val planFragment = PlanFragment()
        val contributeFragment = ContributeFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, discoverFragment, TAG_FRAGMENT_DISCOVER)
        fragmentTransaction.add(R.id.fragment_container, planFragment, TAG_FRAGMENT_PLAN)
        fragmentTransaction.add(R.id.fragment_container, contributeFragment, TAG_FRAGMENT_CONTRIBUTE)
        fragmentTransaction.show(discoverFragment)
        fragmentTransaction.hide(planFragment)
        fragmentTransaction.hide(contributeFragment)
        fragmentTransaction.commit()
        currentFragment = TAG_FRAGMENT_DISCOVER
        action_bar.title = getString(R.string.navi_discover)
    }

    private fun switchTab(itemId: Int): Boolean {
        val fragmentTag = when (itemId) {
            R.id.menu_navi_discover -> TAG_FRAGMENT_DISCOVER
            R.id.menu_navi_plan -> TAG_FRAGMENT_PLAN
            R.id.menu_navi_contribute -> TAG_FRAGMENT_CONTRIBUTE
            else -> return false
        }
        return switchTab(fragmentTag)
    }

    private fun switchTab(fragmentTag: String): Boolean {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (currentFragment == fragmentTag) return false
        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(fragmentTag)
        if (fragment == null) {
            fragment = when (fragmentTag) {
                TAG_FRAGMENT_DISCOVER -> DiscoverFragment()
                TAG_FRAGMENT_PLAN -> PlanFragment()
                TAG_FRAGMENT_CONTRIBUTE -> ContributeFragment()
                else -> return false
            }
            Log.d(TAG, "construct and select new fragment " + fragment.toString())
            fragmentTransaction.add(R.id.fragment_container, fragment, fragmentTag)
        }
        Log.d(TAG, "supportFragmentManager = $supportFragmentManager ${supportFragmentManager.fragments}")
        for (f in supportFragmentManager.fragments) {
            if (f == fragment)
                fragmentTransaction.show(fragment)
            else fragmentTransaction.hide(f)
        }
        fragmentTransaction.commit()
        action_bar.title = getString(when (fragmentTag) {
            TAG_FRAGMENT_DISCOVER -> R.string.navi_discover
            TAG_FRAGMENT_PLAN -> R.string.navi_plan
            TAG_FRAGMENT_CONTRIBUTE -> R.string.navi_contribute
            else -> R.string.app_name
        })
        currentFragment = fragmentTag
        return true
    }

    override fun onTaskClick(item: Task?, viewHolder: TaskViewHolder?) {
        if (item is Task) {
            if (viewHolder is TaskViewHolder) {
                val options = ActivityOptions.makeSceneTransitionAnimation(this,
                        UtilPair.create(viewHolder.cardView, getString(R.string.transition_card)),
                        UtilPair.create(viewHolder.textTitle, getString(R.string.transition_title)),
                        UtilPair.create(viewHolder.linearProperties, getString(R.string.transition_properties)))
                TaskDetailActivity.actionStart(this, item, options)
            } else TaskDetailActivity.actionStart(this, item)
        }
    }

    override fun onTaskButtonStartNowClick(item: Task?) {
        if (item is Task)
            taskViewModel.startTask(item)
    }

    override fun onListCanScrollUpChanged(canScrollDown: Boolean) {
        action_bar.isActivated = canScrollDown
        Log.d(TAG, "action_bar isActivated = ${action_bar.isActivated}")
    }

    override fun onTaskLongClick(item: Task?): Boolean {
        if (item is Task) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, String.format(getString(R.string.text_share_task),
                        item.title, item.narration))
                type = "text/plain"
            }
            startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.title_share_task)))
            return true
        } else return false
    }

    companion object {
        const val TAG = "MainActivity"
        const val TAG_FRAGMENT_DISCOVER = "FragmentDiscover"
        const val TAG_FRAGMENT_PLAN = "FragmentPlan"
        const val TAG_FRAGMENT_CONTRIBUTE = "FragmentContribute"
        const val KEY_CURRENT_FRAGMENT = "key_current_fragment"
    }
}
