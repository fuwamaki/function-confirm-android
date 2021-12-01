package com.example.yusakumaki.functionconfirm

import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.mockito.kotlin.mock
import com.example.yusakumaki.functionconfirm.fragments.ProfileViewModel
import org.junit.runner.RunWith
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class ProfileViewModelTest {

    @Test
    fun testOnAction() {
        val viewModel = ProfileViewModel()
        val observer = mock<Observer<Unit>>()
        viewModel.state.observeForever(observer)
        viewModel.onAction()
        verify(observer)
            .onChanged(Unit)
    }
}