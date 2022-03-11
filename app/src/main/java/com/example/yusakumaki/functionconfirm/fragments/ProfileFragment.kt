package com.example.yusakumaki.functionconfirm.fragments

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yusakumaki.functionconfirm.R
import com.example.yusakumaki.functionconfirm.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by lazy {
//        ViewModelProvider.NewInstanceFactory().create(ProfileViewModel::class.java)
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        val needsStrikeThrough = true
        binding.sampleTextView.apply {
            paint.flags =
                if (needsStrikeThrough) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else Paint.ANTI_ALIAS_FLAG
            paint.isAntiAlias = needsStrikeThrough
            invalidate() // 再描画
        }
        viewModel.state.observe(viewLifecycleOwner) {
            Toast.makeText(context, "tap action", Toast.LENGTH_SHORT).show()
        }
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.view_sample_spinner_item,
            arrayOf(1, 2, 3, 4, 5)
        )
        binding.sampleSpinner.adapter = adapter
        binding.sampleSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val item = (parent as Spinner).selectedItem as Int
                    Log.v("tag", item.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        return binding.root
    }
}