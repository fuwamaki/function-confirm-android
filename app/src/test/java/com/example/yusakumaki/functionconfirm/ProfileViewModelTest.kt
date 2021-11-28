package com.example.yusakumaki.functionconfirm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.Test
import org.mockito.kotlin.mock
import com.example.yusakumaki.functionconfirm.fragments.ProfileViewModel
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ProfileViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testOnAction() {
        val viewModel = ProfileViewModel()
        val observer = mock<Observer<Unit>>()
        viewModel.actionEvent.observeForever(observer)
        viewModel.onAction()
        verify(observer)
                .onChanged(Unit)
    }
}