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
import com.example.yusakumaki.functionconfirm.databinding.FragmentCardPagesBinding
import timber.log.Timber
import kotlin.math.abs

class CardPagesFragment : Fragment(), GestureDetector.OnGestureListener {
    private lateinit var binding: FragmentCardPagesBinding
    private lateinit var mDetector: GestureDetectorCompat

    private val viewModel: CardPagesViewModel by lazy {
        ViewModelProvider(this)[CardPagesViewModel::class.java]
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
            ObjectAnimator.ofFloat(binding.sampleView, View.ALPHA, 1.0f, 0.0f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView, View.ROTATION, 0f, -5f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView, "translationX", -20f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView2, View.ROTATION, 5f, 0f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView2, "translationX", -20f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView3, View.ROTATION, 10f, 5f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView3, "translationX", -20f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView4, View.ROTATION, 15f, 10f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(binding.sampleView4, "translationX", -20f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }
        }

        return binding.root
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
        val MIN_SWIPE_DISTANCE_X = 100
        val MIN_SWIPE_DISTANCE_Y = 100
        val MAX_SWIPE_DISTANCE_X = 2000
        val MAX_SWIPE_DISTANCE_Y = 2000

        val deltaX: Float = p0.x - p1.x
        val deltaY: Float = p0.y - p1.y
        val deltaXAbs = abs(deltaX)
        val deltaYAbs = abs(deltaY)

        if ((deltaXAbs >= MIN_SWIPE_DISTANCE_X) && (deltaXAbs <= MAX_SWIPE_DISTANCE_X)) {
            if (deltaX > 0) {
                Timber.d("batchSwipe: LEFT")
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