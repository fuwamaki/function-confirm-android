package com.example.yusakumaki.functionconfirm.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yusakumaki.functionconfirm.R
import com.example.yusakumaki.functionconfirm.databinding.FragmentCardPagesBinding
import com.example.yusakumaki.functionconfirm.extension.HINDU_CARD_DURATION
import com.example.yusakumaki.functionconfirm.extension.HINDU_ROTATE_VALUE
import com.example.yusakumaki.functionconfirm.extension.hinduFadeIn
import com.example.yusakumaki.functionconfirm.extension.hinduFadeOut
import com.example.yusakumaki.functionconfirm.extension.hinduRotate
import com.example.yusakumaki.functionconfirm.extension.hinduTranslationX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.abs


class CardPagesFragment : Fragment(), GestureDetector.OnGestureListener {
    companion object {
        private const val MIN_SWIPE_DISTANCE_X = 100
        private const val MIN_SWIPE_DISTANCE_Y = 100
        private const val MAX_SWIPE_DISTANCE_X = 2000
        private const val MAX_SWIPE_DISTANCE_Y = 2000
    }

    private val viewModel: CardPagesViewModel by lazy {
        ViewModelProvider(this)[CardPagesViewModel::class.java]
    }

    private lateinit var binding: FragmentCardPagesBinding
    private lateinit var mDetector: GestureDetectorCompat
    private var cardViews: Array<View> = arrayOf()

    private val cardViewsParSize: Int
        get() = cardViews.size / 3

    private val firstView: View
        get() = layoutInflater.inflate(R.layout.view_first_card, binding.cardsFrameLayout, false)

    private val secondView: View
        get() = layoutInflater.inflate(R.layout.view_second_card, binding.cardsFrameLayout, false)

    private val thirdView: View
        get() = layoutInflater.inflate(R.layout.view_third_card, binding.cardsFrameLayout, false)

    private val fourthView: View
        get() = layoutInflater.inflate(R.layout.view_fourth_card, binding.cardsFrameLayout, false)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCardPagesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        mDetector = GestureDetectorCompat(requireContext(), this)
        binding.root.setOnTouchListener { _, motionEvent ->
            mDetector.onTouchEvent(motionEvent)
        }

        viewModel.onCreate()

        cardViews = arrayOf(
            firstView, secondView, thirdView, fourthView, // 非表示部
            firstView, secondView, thirdView, fourthView, // 表示部
            firstView, secondView, thirdView, fourthView, // 非表示部
        )
        cardViews.reversed().forEachIndexed { index, view ->
            val computeIndex = (cardViewsParSize * 2 - (index + 1))
            binding.cardsFrameLayout.addView(view)
            view.translationX = resources.getDimension(R.dimen.space_16) +
                    resources.getDimension(R.dimen.space_28) * computeIndex
            view.rotation = computeIndex * HINDU_ROTATE_VALUE
            view.alpha = if (index / cardViewsParSize == 1) 1f else 0f
        }

        binding.button.setOnClickListener {
            swipeLeftAnimation()
        }

        return binding.root
    }

    private var isAnimationRunning = false

    private fun swipeLeftAnimation() {
        if (isAnimationRunning) return
        isAnimationRunning = true

        cardViews.forEachIndexed { index, view ->
            val translateValue = resources.getDimension(R.dimen.space_28)
            val rotateValue = HINDU_ROTATE_VALUE
            if (index == 0) { // 1番手前のカード（非表示中）を1番奥のカード位置に移動
                view.hinduTranslationX(translateValue * (cardViews.size - 1))
                view.hinduRotate(rotateValue * (cardViews.size - 1))
            } else if (index / cardViewsParSize == 1 && index % cardViewsParSize == 0) { // 回転&非表示にする
                view.hinduTranslationX(-translateValue)
                view.hinduRotate(-rotateValue)
                view.hinduFadeOut()
            } else if (index / cardViewsParSize == 2 && index % cardViewsParSize == 0) { // 回転&表示にする
                view.hinduTranslationX(-translateValue)
                view.hinduRotate(-rotateValue)
                view.hinduFadeIn()
            } else { // 回転
                view.hinduTranslationX(-translateValue)
                view.hinduRotate(-rotateValue)
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(HINDU_CARD_DURATION)
            // 1番手前のカードを一番背面に移動（=1番手前以外をすべて前面に）
            cardViews.reversed().forEachIndexed { index, view ->
                if (index != 0) {
                    binding.cardsFrameLayout.bringChildToFront(view)
                }
            }
            // 1番手前のカードを1番奥にする順番に変更
            cardViews = cardViews.copyOfRange(1, cardViews.size) + cardViews.first()
            isAnimationRunning = false
        }
    }

    private fun swipeRightAnimation() {
        if (isAnimationRunning) return
        isAnimationRunning = true

        cardViews.reversed().forEachIndexed { index, view ->
            val translateValue = resources.getDimension(R.dimen.space_28)
            val rotateValue = HINDU_ROTATE_VALUE
            if (index == 0) { // 1番奥のカード（非表示中）を1番手前のカード位置に移動
                view.hinduTranslationX(-translateValue * (cardViews.size - 1))
                view.hinduRotate(-rotateValue * (cardViews.size - 1))
            } else if (index / cardViewsParSize == 1 && index % cardViewsParSize == 0) { // 回転&非表示にする
                view.hinduTranslationX(translateValue)
                view.hinduRotate(rotateValue)
                view.hinduFadeOut()
            } else if (index / cardViewsParSize == 2 && index % cardViewsParSize == 0) { // 回転&表示にする
                view.hinduTranslationX(translateValue)
                view.hinduRotate(rotateValue)
                view.hinduFadeIn()
            } else { // 回転
                view.hinduTranslationX(translateValue)
                view.hinduRotate(rotateValue)
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(HINDU_CARD_DURATION)
            // 1番奥のカードを一番前面に移動
            cardViews.last().bringToFront()
            // 1番奥のカードを1番手前にする順番に変更
            cardViews = arrayOf(cardViews.last()) + cardViews.copyOfRange(0, cardViews.size - 1)
            isAnimationRunning = false
        }
    }

    override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        val deltaX: Float = p0.x - p1.x
        val deltaY: Float = p0.y - p1.y
        val deltaXAbs = abs(deltaX)
        val deltaYAbs = abs(deltaY)
        if ((deltaXAbs >= MIN_SWIPE_DISTANCE_X) && (deltaXAbs <= MAX_SWIPE_DISTANCE_X)) {
            if (deltaX > 0) {
                swipeLeftAnimation()
            } else {
                swipeRightAnimation()
            }
        } else if ((deltaYAbs >= MIN_SWIPE_DISTANCE_Y) && (deltaYAbs <= MAX_SWIPE_DISTANCE_Y)) {
            if (deltaY > 0) {
                Timber.d("batchSwipe: UP")
            } else {
                Timber.d("batchSwipe: DOWN")
            }
        }
        return true
    }

    override fun onShowPress(p0: MotionEvent) {}
    override fun onLongPress(p0: MotionEvent) {}
    override fun onDown(p0: MotionEvent): Boolean {
        return true
    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        return true
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return true
    }
}