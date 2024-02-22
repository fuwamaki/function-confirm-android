package com.example.yusakumaki.functionconfirm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        return binding.root
    }
}