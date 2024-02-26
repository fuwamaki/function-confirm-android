package com.example.yusakumaki.functionconfirm.extension

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator

const val HINDU_CARD_DURATION = 200L
const val HINDU_ROTATE_VALUE = 5f

/**
 * Hindu Animation
 * ※語源: 一般的なトランプのシャッフルを Hindu Shuffle と呼ぶらしいので。
 */
fun View.hinduRotate(value: Float, durationValue: Long = HINDU_CARD_DURATION) {
    ObjectAnimator.ofFloat(this, View.ROTATION, this.rotation, this.rotation+value).apply {
        duration = durationValue
        interpolator = AccelerateInterpolator()
        start()
    }
}

fun View.hinduTranslationX(value: Float, durationValue: Long = HINDU_CARD_DURATION) {
    ObjectAnimator.ofFloat(this, View.TRANSLATION_X, this.translationX, this.translationX+value).apply {
        duration = durationValue
        interpolator = AccelerateInterpolator()
        start()
    }
}

fun View.hinduFadeIn(durationValue: Long = HINDU_CARD_DURATION) {
    ObjectAnimator.ofFloat(this, View.ALPHA, this.alpha, 1f).apply {
        duration = durationValue
        interpolator = AccelerateInterpolator()
        start()
    }
}

fun View.hinduFadeOut(durationValue: Long = HINDU_CARD_DURATION) {
    ObjectAnimator.ofFloat(this, View.ALPHA, this.alpha, 0f).apply {
        duration = durationValue
        interpolator = AccelerateInterpolator()
        start()
    }
}