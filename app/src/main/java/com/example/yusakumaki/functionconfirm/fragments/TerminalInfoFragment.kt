package com.example.yusakumaki.functionconfirm.fragments

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yusakumaki.functionconfirm.databinding.FragmentTerminalInfoBinding
import com.google.android.material.transition.MaterialContainerTransform

class TerminalInfoFragment : Fragment() {

    private lateinit var binding: FragmentTerminalInfoBinding

    private val viewModel: TerminalInfoViewModel by lazy {
        ViewModelProvider(this)[TerminalInfoViewModel::class.java]
    }

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grant ->

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transform = MaterialContainerTransform()
        transform.duration = 1000
        sharedElementEnterTransition = transform
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentTerminalInfoBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // 位置情報の認証リクエスト
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        viewModel.requestAdvertisingId()
        viewModel.fetchGlobalIPAddress()
        viewModel.fetchLocalIPAddress()
        viewModel.requestWifiInfo()

        return binding.root
    }
}