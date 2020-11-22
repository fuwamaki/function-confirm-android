package com.example.yusakumaki.functionconfirm.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yusakumaki.functionconfirm.Components.HomeGridAdapter
import com.example.yusakumaki.functionconfirm.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val adapter = HomeGridAdapter(requireContext())
        binding.mainGridView.adapter = adapter
        binding.mainGridView.numColumns = 2
        binding.mainGridView.horizontalSpacing = 1
        binding.mainGridView.verticalSpacing = 1
        return binding.root
    }
}