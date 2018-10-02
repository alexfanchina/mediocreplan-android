package com.race.mediocreplan.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.race.mediocreplan.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val fragmentMap: HashMap<Int, Fragment> = HashMap()
    private var currentFragment: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO: UI bug when rotating screen
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            switchTab(menuItem.itemId)
        }
        initTabs()
    }

    private fun initTabs() {
        val discoverFragment = DiscoverFragment()
        val contributeFragment = ContributeFragment()
        fragmentMap[R.id.menu_navi_discover] = discoverFragment
        fragmentMap[R.id.menu_navi_contribute] = contributeFragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, discoverFragment)
        fragmentTransaction.add(R.id.fragment_container, contributeFragment)
        fragmentTransaction.show(discoverFragment)
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

    companion object {
        const val TAG = "MainActivity"
    }
}
