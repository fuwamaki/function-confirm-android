package com.example.yusakumaki.functionconfirm.Fragments

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yusakumaki.functionconfirm.R
import com.example.yusakumaki.functionconfirm.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val needsStrikeThrough = true
        binding.sampleTextView.apply {
            paint.flags = if (needsStrikeThrough) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else Paint.ANTI_ALIAS_FLAG
            paint.isAntiAlias = needsStrikeThrough
            invalidate() // 再描画
        }
        return binding.root
    }
}