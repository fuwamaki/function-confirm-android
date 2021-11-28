package com.example.yusakumaki.functionconfirm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.yusakumaki.functionconfirm.R
import com.example.yusakumaki.functionconfirm.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // setup binding
        binding = FragmentMainBinding.inflate(inflater, container, false)
        // setup view
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        binding.mainSampleButton.setOnClickListener {
            val extras = FragmentNavigatorExtras(view to "shared_element_container")
            findNavController().navigate(R.id.show_second_fragment, null, null, extras)
        }
        return view
    }
}