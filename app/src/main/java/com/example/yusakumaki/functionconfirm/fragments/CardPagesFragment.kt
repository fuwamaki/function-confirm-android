package com.example.yusakumaki.functionconfirm.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yusakumaki.functionconfirm.databinding.FragmentCardPagesBinding

class CardPagesFragment : Fragment() {
    private lateinit var binding: FragmentCardPagesBinding

    private val viewModel: CardPagesViewModel by lazy {
        ViewModelProvider(this)[CardPagesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCardPagesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.onCreate()

        binding.button.setOnClickListener {
            ObjectAnimator.ofFloat(binding.sampleView, View.ALPHA, 1.0f, 0.0f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView, View.ROTATION, 0f, -5f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView, "translationX", -20f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView2, View.ROTATION, 5f, 0f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView2, "translationX", -20f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView3, View.ROTATION, 10f, 5f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView3, "translationX", -20f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView4, View.ROTATION, 15f, 10f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView4, "translationX", -20f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }
        }

        return binding.root
    }
}