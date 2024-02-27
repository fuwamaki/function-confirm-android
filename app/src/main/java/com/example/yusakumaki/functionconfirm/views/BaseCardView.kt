package com.example.yusakumaki.functionconfirm.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseCardView: ConstraintLayout {

    private var _binding: ViewDataBinding? = null

    constructor(context: Context): super(context) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize(context)
    }

    private fun initialize(context: Context) {
        val root = LayoutInflater.from(context).inflate(getLayoutId, this, false)
        _binding = DataBindingUtil.bind(root)
        addView(root)
    }

    abstract val getLayoutId: Int

    abstract fun placedToFront()
}