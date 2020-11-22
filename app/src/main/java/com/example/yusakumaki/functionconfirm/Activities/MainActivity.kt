package com.example.yusakumaki.functionconfirm.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.*
import androidx.navigation.ui.NavigationUI
import com.example.yusakumaki.functionconfirm.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.main_navigation_host)
        NavigationUI.setupWithNavController(bottom_navigation, navController)
    }
}