package com.example.yusakumaki.functionconfirm.views

import android.content.Context
import com.example.yusakumaki.functionconfirm.R
import timber.log.Timber

class FourthCardView(context: Context) : BaseCardView(context) {
    override val getLayoutId: Int
        get() = R.layout.view_fourth_card

    override fun placedToFront() {
        Timber.d("FourthCardView placedToFront")
    }
}