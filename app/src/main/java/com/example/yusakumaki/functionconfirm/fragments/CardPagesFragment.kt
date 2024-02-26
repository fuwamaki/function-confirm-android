package com.example.yusakumaki.functionconfirm.fragments

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yusakumaki.functionconfirm.R
import com.example.yusakumaki.functionconfirm.databinding.FragmentCardPagesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.abs

class CardPagesFragment : Fragment(), GestureDetector.OnGestureListener {
    companion object {
        private const val TAKE_CARD_DURATION = 200L
        private const val BRING_CHILD_TO_BACK_DURATION = 100L
        private const val BRING_CHILD_TO_BACK_DELAY = 210L
        private const val MIN_SWIPE_DISTANCE_X = 100
        private const val MIN_SWIPE_DISTANCE_Y = 100
        private const val MAX_SWIPE_DISTANCE_X = 2000
        private const val MAX_SWIPE_DISTANCE_Y = 2000
    }

    enum class CardPageType {
        First, Second, Third, Fourth
    }

    private lateinit var binding: FragmentCardPagesBinding
    private lateinit var mDetector: GestureDetectorCompat
    private var cardPageTypes: Array<CardPageType> = arrayOf(
        CardPageType.First,
        CardPageType.Second,
        CardPageType.Third,
        CardPageType.Fourth
    )

    private val viewModel: CardPagesViewModel by lazy {
        ViewModelProvider(this)[CardPagesViewModel::class.java]
    }

    private fun cardView(cardPageType: CardPageType): View {
        return when (cardPageType) {
            CardPageType.First -> binding.sampleView
            CardPageType.Second -> binding.sampleView2
            CardPageType.Third -> binding.sampleView3
            CardPageType.Fourth -> binding.sampleView4
        }
    }

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

        binding.button.setOnClickListener {
            swipeLeftAnimation()
        }

        return binding.root
    }

    private var isAnimationRunning = false

    private fun swipeLeftAnimation() {
        if (isAnimationRunning) return
        isAnimationRunning = true
        // first: fade out
        ObjectAnimator.ofFloat(cardView(cardPageTypes[0]), View.ALPHA, 1.0f, 0.0f).apply {
            duration = TAKE_CARD_DURATION
            interpolator = AccelerateInterpolator()
            start()
        }

        // first: rotate
        ObjectAnimator.ofFloat(cardView(cardPageTypes[0]), View.ROTATION, 0f, -5f).apply {
            duration = TAKE_CARD_DURATION
            interpolator = AccelerateInterpolator()
            start()
        }

        // first: translate
        ObjectAnimator.ofFloat(cardView(cardPageTypes[0]), "translationX", cardView(cardPageTypes[0]).translationX-resources.getDimension(R.dimen.size_25)).apply {
            duration = TAKE_CARD_DURATION
            interpolator = AccelerateInterpolator()
            start()
        }

        // first: 動作後、一番裏に移動
        ObjectAnimator.ofFloat(cardView(cardPageTypes[0]), View.ALPHA, 0.0f, 1.0f).apply {
            duration = BRING_CHILD_TO_BACK_DURATION
            startDelay = BRING_CHILD_TO_BACK_DELAY
            start()
        }
        ObjectAnimator.ofFloat(cardView(cardPageTypes[0]), View.ROTATION, -5f, 15f).apply {
            duration = 0
            startDelay = TAKE_CARD_DURATION
            start()
        }
        ObjectAnimator.ofFloat(cardView(cardPageTypes[0]), "translationX", cardView(cardPageTypes[0]).translationX+resources.getDimension(R.dimen.size_25)*3).apply {
            duration = 0
            startDelay = TAKE_CARD_DURATION
            start()
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(TAKE_CARD_DURATION)
            binding.cardsFrameLayout.bringChildToFront(cardView(cardPageTypes[3]))
            binding.cardsFrameLayout.bringChildToFront(cardView(cardPageTypes[2]))
            binding.cardsFrameLayout.bringChildToFront(cardView(cardPageTypes[1]))
            delay(100)
            cardPageTypes = arrayOf(
                cardPageTypes[1],
                cardPageTypes[2],
                cardPageTypes[3],
                cardPageTypes[0],
            )
            isAnimationRunning = false
        }

        // second: rotate
        ObjectAnimator.ofFloat(cardView(cardPageTypes[1]), View.ROTATION, 5f, 0f).apply {
            duration = TAKE_CARD_DURATION
            interpolator = AccelerateInterpolator()
            start()
        }

        // second: translate
        ObjectAnimator.ofFloat(cardView(cardPageTypes[1]), "translationX", cardView(cardPageTypes[1]).translationX-resources.getDimension(R.dimen.size_25)).apply {
            duration = TAKE_CARD_DURATION
            interpolator = AccelerateInterpolator()
            start()
        }

        // third: rotate
        ObjectAnimator.ofFloat(cardView(cardPageTypes[2]), View.ROTATION, 10f, 5f).apply {
            duration = TAKE_CARD_DURATION
            interpolator = AccelerateInterpolator()
            start()
        }

        // third: translate
        ObjectAnimator.ofFloat(cardView(cardPageTypes[2]), "translationX", cardView(cardPageTypes[2]).translationX-resources.getDimension(R.dimen.size_25)).apply {
            duration = TAKE_CARD_DURATION
            interpolator = AccelerateInterpolator()
            start()
        }

        // fourth: rotate
        ObjectAnimator.ofFloat(cardView(cardPageTypes[3]), View.ROTATION, 15f, 10f).apply {
            duration = TAKE_CARD_DURATION
            interpolator = AccelerateInterpolator()
            start()
        }

        // fourth: translate
        ObjectAnimator.ofFloat(cardView(cardPageTypes[3]), "translationX", cardView(cardPageTypes[3]).translationX-resources.getDimension(R.dimen.size_25)).apply {

            duration = TAKE_CARD_DURATION
            interpolator = AccelerateInterpolator()
            start()
        }
    }

    override fun onDown(p0: MotionEvent): Boolean {
        Timber.d("OnGestureListener: onDown")
        return true
    }

    override fun onShowPress(p0: MotionEvent) {
        Timber.d("OnGestureListener: onShowPress")
    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        Timber.d("OnGestureListener: onSingleTapUp")
        return true
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        Timber.d("OnGestureListener: onScroll")
        return true
    }

    override fun onLongPress(p0: MotionEvent) {
        Timber.d("OnGestureListener: onLongPress")
    }

    override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        Timber.d("OnGestureListener: onFling")
        val deltaX: Float = p0.x - p1.x
        val deltaY: Float = p0.y - p1.y
        val deltaXAbs = abs(deltaX)
        val deltaYAbs = abs(deltaY)

        if ((deltaXAbs >= MIN_SWIPE_DISTANCE_X) && (deltaXAbs <= MAX_SWIPE_DISTANCE_X)) {
            if (deltaX > 0) {
                Timber.d("batchSwipe: LEFT")
                swipeLeftAnimation()
            } else {
                Timber.d("batchSwipe: RIGHT")
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
}