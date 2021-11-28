package com.example.yusakumaki.functionconfirm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yusakumaki.functionconfirm.R
import com.google.android.material.transition.MaterialContainerTransform

class SecondFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transform = MaterialContainerTransform()
        transform.duration = 1000
        sharedElementEnterTransition = transform
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_second, container, false)
    }
}