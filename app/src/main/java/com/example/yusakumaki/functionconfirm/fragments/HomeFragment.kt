package com.example.yusakumaki.functionconfirm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.yusakumaki.functionconfirm.components.HomeGridAdapter
import com.example.yusakumaki.functionconfirm.entity.gridItems
import com.example.yusakumaki.functionconfirm.R
import com.example.yusakumaki.functionconfirm.databinding.FragmentHomeBinding
import com.google.android.material.transition.MaterialElevationScale

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
        binding.mainGridView.setOnItemClickListener { _, view, position, _ ->
            val imageView = view.findViewById<ImageView>(R.id.item_image_view)
            Toast.makeText(context, "fragment: click:" + gridItems[position].title, Toast.LENGTH_SHORT).show()
            val extras = FragmentNavigatorExtras(imageView to "shared_element_image_view")
            findNavController().navigate(R.id.show_second_fragment, null, null, extras)
        }
        exitTransition = MaterialElevationScale(true)
        return binding.root
    }
}