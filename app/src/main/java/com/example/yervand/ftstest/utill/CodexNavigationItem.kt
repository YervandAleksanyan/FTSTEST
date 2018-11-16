package com.example.yervand.ftstest.utill

import android.animation.*
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.yervand.ftstest.R
import android.view.animation.AccelerateDecelerateInterpolator
import com.robertlevonyan.components.kex.onEnd


class CodexNavigationItem : FrameLayout {

    enum class ScrollDirection {
        DOWN,
        UP
    }

    private lateinit var progressBar: ProgressBar
    private lateinit var mainTextView: TextView
    private lateinit var secondTextView: TextView
    private lateinit var progressAnimation: ValueAnimator
    private var progressValue: Int? = 0

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        View.inflate(context, R.layout.codex_navigation_item, this)
        initViews()
    }

    private fun initViews() {
        progressBar = findViewById(R.id.progress_bar)
        mainTextView = findViewById(R.id.progress_value)
        secondTextView = findViewById(R.id.progress_value_second)
        secondTextView.visibility = View.INVISIBLE
        initProgressAnimation()
    }

    fun setProgressValue(value: Int?) {
        if (value != null) {
            progressAnimation.setValues(PropertyValuesHolder.ofInt("progress", progressBar.progress, value))
            progressAnimation.start()
        } else {
            progressAnimation.setValues(PropertyValuesHolder.ofInt("progress", 0))
            progressAnimation.start()
        }
    }

    fun setItemText(text: String, scrollDirection: ScrollDirection) {

        if (mainTextView.text != text) {
            val auxLabelOffset: Float = when (scrollDirection) {
                ScrollDirection.DOWN -> mainTextView.height / 2.0F
                else -> -mainTextView.height / 2.0F
            }

            //Second View Animation
            secondTextView.text = text
            secondTextView.translationY = auxLabelOffset
            secondTextView.scaleY = 0.1F
            secondTextView.scaleX = 1F
            secondTextView.visibility = View.VISIBLE

            //Main View animation
            val translationAnim2 = ObjectAnimator.ofFloat(mainTextView, "translationY", -auxLabelOffset)
                    .apply {
                        duration = 500
                    }

            val scaleYAnim2 = ObjectAnimator.ofFloat(mainTextView, "scaleY", 0.1F)
                    .apply {
                        duration = 500
                    }
            val scaleXAnim2 = ObjectAnimator.ofFloat(mainTextView, "scaleX", 1F)
                    .apply {
                        duration = 500
                    }
            val translationAnim3 = ObjectAnimator.ofFloat(secondTextView, "translationY", 0F)
                    .apply {
                        duration = 500
                    }

            val scaleYAnim3 = ObjectAnimator.ofFloat(secondTextView, "scaleY", 1F)
                    .apply {
                        duration = 500
                    }
            val scaleXAnim3 = ObjectAnimator.ofFloat(secondTextView, "scaleX", 1F)
                    .apply {
                        duration = 500
                    }
            val transform = AnimatorSet()
            transform.interpolator = AccelerateDecelerateInterpolator()
            transform.playTogether(
                    translationAnim2,
                    scaleXAnim2,
                    scaleYAnim2,
                    translationAnim3,
                    scaleXAnim3,
                    scaleYAnim3
            )
            transform.start()
            transform.onEnd {
                mainTextView.text = secondTextView.text
                mainTextView.translationY = 0F
                mainTextView.scaleY = 1F
                mainTextView.scaleX = 1F
                secondTextView.visibility = View.INVISIBLE
            }
        }
    }

    private fun initProgressAnimation() {
        progressAnimation = ValueAnimator.ofInt(progressBar.progress, progressValue!!)
                .apply {
                    duration = 1000
                    interpolator = AccelerateDecelerateInterpolator()
                }
        progressAnimation.addUpdateListener {
            progressBar.progress = it.animatedValue as Int
        }
    }

    private fun createTranslationAnimation(view: TextView, vararg values: Float): ObjectAnimator =
            ObjectAnimator.ofFloat(view,
                    "translationY", values.first(), values.last())
                    .apply {
                        duration = 1000
                    }


    fun progressAnimationStop() {
        progressAnimation.pause()
    }


}