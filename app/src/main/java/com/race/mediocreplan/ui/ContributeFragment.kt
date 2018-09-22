package com.race.mediocreplan.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.race.mediocreplan.R
import com.race.mediocreplan.ui.utils.SpannedUtils
import kotlinx.android.synthetic.main.fragment_contribute.view.*


class ContributeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contribute, container, false)
        view.text.text = SpannedUtils.fromHtml(getString(R.string.why_mediocre))
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
        fun newInstance(param1: String, param2: String) =
                ContributeFragment().apply {}
    }
}
