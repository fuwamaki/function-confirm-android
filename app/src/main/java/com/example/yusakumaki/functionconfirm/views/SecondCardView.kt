package com.example.yusakumaki.functionconfirm.views

import android.content.Context
import com.example.yusakumaki.functionconfirm.R
import timber.log.Timber

class SecondCardView(context: Context) : BaseCardView(context) {
    override val getLayoutId: Int
        get() = R.layout.view_second_card

    override fun placedToFront() {
        Timber.d("SecondCardView placedToFront")
    }
}