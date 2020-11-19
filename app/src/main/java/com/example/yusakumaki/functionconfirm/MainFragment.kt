package com.example.yusakumaki.functionconfirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        view.main_sample_button.setOnClickListener {
            val extras = FragmentNavigatorExtras(view to "shared_element_container")
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment, null, null, extras)
        }
        return view
    }
}