package com.example.yusakumaki.functionconfirm.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yusakumaki.functionconfirm.R
import com.example.yusakumaki.functionconfirm.databinding.FragmentProfileBinding
import com.example.yusakumaki.functionconfirm.helper.StepCountHelper
import timber.log.Timber

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
        binding.lifecycleOwner = this

        viewModel.updatePermission()
        viewModel.requestAdvertisingId()
        viewModel.fetchGlobalIPAddress()
        viewModel.fetchLocalIPAddress()
        viewModel.requestWifiInfo()

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
        viewModel.checkGoogleSignIn.observe(viewLifecycleOwner) {
            StepCountHelper.checkGoogleSignIn(this) {
                StepCountHelper.checkActivityPermission(requireActivity()) {
                    Toast.makeText(context, "checkGoogleFitPermission", Toast.LENGTH_SHORT).show()
                    viewModel.updatePermission()
                }
            }
        }
        viewModel.openSettingEvent.observe(viewLifecycleOwner) {
            val uriString = "package:${requireContext().packageName}"
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse(uriString)
            )
            startActivity(intent)
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
                    Timber.d("item: $item")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        binding.sampleImage.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.sample_item_menu -> {
                        Toast.makeText(context, "tap", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.sample_menu)
            popupMenu.show()
        }
        return binding.root
    }

    @Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == StepCountHelper.GOOGLE_SIGN_IN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            StepCountHelper.checkActivityPermission(requireActivity()) {
                Toast.makeText(context, "checkGoogleFitPermission", Toast.LENGTH_SHORT).show()
                viewModel.updatePermission()
            }
        } else if (requestCode == StepCountHelper.GOOGLE_FIT_PERMISSION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Toast.makeText(context, "googleFitResultLauncher", Toast.LENGTH_SHORT).show()
            viewModel.updatePermission()
        }
    }
}