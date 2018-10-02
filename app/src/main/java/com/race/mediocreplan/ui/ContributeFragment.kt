package com.race.mediocreplan.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.race.mediocreplan.R
import kotlinx.android.synthetic.main.fragment_contribute.view.*
import android.content.Intent
import android.net.Uri


class ContributeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contribute, container, false)
        view.button_send_email.setOnClickListener { _ ->
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "takeasnaprace@gmail.com", null))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contribute to Mediocre Plan")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "")
            startActivity(Intent.createChooser(emailIntent, getString(R.string.title_send_email_intent)))
        }
        view.text_navi_intro.setOnClickListener { _ ->
            IntroActivity.actionStart(context!!)
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ContributeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                ContributeFragment().apply {}
    }
}
