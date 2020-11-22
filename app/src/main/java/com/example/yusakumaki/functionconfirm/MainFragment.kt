package com.example.yusakumaki.functionconfirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.yusakumaki.functionconfirm.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // setup binding
        binding = FragmentMainBinding.inflate(inflater, container, false)
        // setup view
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        view.main_sample_button.setOnClickListener {
            val extras = FragmentNavigatorExtras(view to "shared_element_container")
            findNavController().navigate(R.id.show_second_fragment, null, null, extras)
        }
        return view
    }
}