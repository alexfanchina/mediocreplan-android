package com.race.mediocreplan.ui

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.race.mediocreplan.R
import com.race.mediocreplan.ui.utils.SpannedUtils
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        scroll.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            action_bar.isActivated = scroll.canScrollVertically(-1)
        }
        text.text = SpannedUtils.fromHtml(getString(R.string.why_mediocre))
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, IntroActivity::class.java)
            context.startActivity(intent)
        }
    }
}
