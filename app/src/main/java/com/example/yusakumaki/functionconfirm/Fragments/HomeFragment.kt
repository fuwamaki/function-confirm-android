package com.example.yusakumaki.functionconfirm.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.yusakumaki.functionconfirm.Components.HomeGridAdapter
import com.example.yusakumaki.functionconfirm.Entity.gridItems
import com.example.yusakumaki.functionconfirm.R
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
        binding.mainGridView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(context, "fragment: click:" + gridItems[position].title, Toast.LENGTH_SHORT).show()
            val extras = FragmentNavigatorExtras(view to "shared_element_container")
            findNavController().navigate(R.id.show_second_fragment, null, null, extras)
        }
        return binding.root
    }
}