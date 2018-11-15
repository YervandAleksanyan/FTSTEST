package com.example.yervand.ftstest.utill

import android.animation.*
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.yervand.ftstest.R
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.transition.TransitionManager
import com.robertlevonyan.components.kex.onEnd
import android.view.animation.ScaleAnimation
import android.R.attr.y
import android.R.attr.x


class CodexNavigationItem : FrameLayout {
    private lateinit var progressBar: ProgressBar
    private lateinit var itemText: TextView
    private lateinit var itemTextSecond: TextView
    private lateinit var currentText: TextView
    private lateinit var progressAnimation: ValueAnimator
    private var progressValue: Int? = 0
    private val circularAnimation = AnimatorSet()

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
        itemText = findViewById(R.id.progress_value)
        itemTextSecond = findViewById(R.id.progress_value_second)
        itemTextSecond.visibility = View.INVISIBLE
        currentText = itemTextSecond
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

        if (itemText.text != text) {
            val auxLabelOffset: Double = when (scrollDirection) {
                ScrollDirection.UP -> -progressBar.height / 5.0
                else -> progressBar.height / 5.0
            }

            itemTextSecond.text = text
            itemTextSecond
                    .animate()
                    .translationY(auxLabelOffset.toFloat())
                    .scaleX(1F)
                    .scaleY(0.1F)
                    .start()

            itemTextSecond.visibility = View.VISIBLE

            val animation: ViewPropertyAnimator = itemText.animate()
                    .setDuration(500)
                    .translationY(-auxLabelOffset.toFloat())
                    .scaleX(1F)
                    .scaleY(0.1F)
            animation.start()
            animation.setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    itemText.text = itemTextSecond.text
                    itemText
                            .animate()
                            .scaleX(1F)
                            .scaleY(1F)
                            .translationY(0F)
                            .start()
                    itemTextSecond.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                    itemTextSecond
                            .animate()
                            .translationY(0F)
                            .scaleX(1F)
                            .scaleY(0.1F)
                            .start()
                }
            })
        }

//        val upAnimation: ObjectAnimator?
//        val downAnimation: ObjectAnimator?
//        if (!text.isEmpty()) {
//            restoreViewsAlpha()
//            if (currentText == itemText) {
//                itemText.text = text
//                currentText = itemTextSecond
//            } else {
//                itemTextSecond.text = text
//                currentText = itemText
//            }
//
//            if (currentText.text != text) {
//                when (scrollDirection) {
//                    ScrollDirection.UP -> {
//                        upAnimation = createTranslationAnimation(currentText, 0F, progressBar.height.toFloat())
//
//                        downAnimation = createTranslationAnimation(if (currentText == itemText) itemTextSecond else itemText,
//                                -progressBar.height.toFloat(), 0F)
//
//                    }
//                    else -> {
//                        upAnimation = createTranslationAnimation(currentText, 0F, -progressBar.height.toFloat())
//
//                        downAnimation = createTranslationAnimation(if (currentText == itemText) itemTextSecond else itemText, progressBar.height.toFloat(), 0F)
//                    }
//                }
//                createCircularAnimation(scrollDirection, upAnimation, downAnimation)
//            }
//        } else {
//            textAlphaAnimation(itemTextSecond)
//            textAlphaAnimation(itemText)
//        }
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

    private fun createCircularAnimation(scrollDirection: ScrollDirection, vararg translationAnimation: ObjectAnimator) {
        circularAnimation.duration = 1000
        circularAnimation.playTogether(translationAnimation.asList())
        circularAnimation.start()
        circularAnimation.onEnd {
            currentText.translationY =
                    if (ScrollDirection.UP == scrollDirection) progressBar.height.toFloat()
                    else -progressBar.height.toFloat()
        }
    }

    fun progressAnimationStop() {
        progressAnimation.pause()
    }

    enum class ScrollDirection {
        DOWN,
        UP
    }

    private fun textAlphaAnimation(view: TextView) {
        val animation = ObjectAnimator.ofFloat(view, "alpha", 1F, 0F)
                .apply {
                    duration = 1000
                    interpolator = AccelerateDecelerateInterpolator()
                }
        animation.onEnd {
            view.text = ""
        }
        animation.start()
    }

    private fun restoreViewsAlpha() {
        itemText.alpha = 1F
        itemTextSecond.alpha = 1F
    }
}