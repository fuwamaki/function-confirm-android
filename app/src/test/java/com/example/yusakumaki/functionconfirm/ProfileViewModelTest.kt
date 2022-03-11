package com.example.yusakumaki.functionconfirm

import android.app.Application
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.mockito.kotlin.mock
import com.example.yusakumaki.functionconfirm.fragments.ProfileViewModel
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class ProfileViewModelTest {

    @Test
    fun testOnAction() {
        val mockApplication = Mockito.mock(Application::class.java)
        val viewModel = ProfileViewModel(mockApplication)
        val observer = mock<Observer<Unit>>()
        viewModel.state.observeForever(observer)
        viewModel.onAction()
        verify(observer)
            .onChanged(Unit)
    }
}