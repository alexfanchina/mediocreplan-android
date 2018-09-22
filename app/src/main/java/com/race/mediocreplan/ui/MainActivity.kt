package com.race.mediocreplan.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.race.mediocreplan.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_contribute.view.*

class MainActivity : AppCompatActivity() {

    private val fragmentMap: HashMap<Int, Fragment> = HashMap()
    private var currentFragment: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            switchTab(menuItem.itemId)
        }
        switchTab(R.id.menu_navi_discover)
    }

    private fun switchTab(itemId: Int): Boolean {
        if (currentFragment == itemId) return false
        val fragment: Fragment?
        if (fragmentMap[itemId] != null) {
            fragment = fragmentMap[itemId]
        } else {
            fragment = when (itemId) {
                R.id.menu_navi_discover -> ExploreFragment()
                R.id.menu_navi_contribute -> ContributeFragment()
                else -> return false
            }
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
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
            currentFragment = itemId
            true
        } else false
    }
}
