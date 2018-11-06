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
import android.view.animation.Animation


class CodexNavigationItem : FrameLayout {

    private lateinit var progressBar: ProgressBar
    private lateinit var itemText: TextView
    private lateinit var itemTextSecond: TextView
    private lateinit var currentText: TextView
    private lateinit var progressAnimation: ValueAnimator
    private var progressValue: Int? = 0
    private val set = AnimatorSet()

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
        itemTextSecond.translationY = progressBar.height.toFloat()
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
        val upAnimation: ObjectAnimator?
        val downAnimation: ObjectAnimator?
        if (!text.isEmpty()) {
            if (currentText.text != text) {

                when (scrollDirection) {
                    ScrollDirection.UP -> {
                        upAnimation =
                                ObjectAnimator.ofFloat(currentText,
                                        "translationY", 0F, progressBar.height.toFloat())
                                        .apply {
                                            repeatCount = Animation.INFINITE
                                            duration = 1000
                                        }
                        downAnimation =
                                ObjectAnimator.ofFloat(if (currentText == itemText) itemTextSecond else itemText, "translationY", -progressBar.height.toFloat(), 0F)
                                        .apply {
                                            repeatCount = Animation.INFINITE
                                            duration = 1000
                                        }
                    }
                    else -> {
                        upAnimation =
                                ObjectAnimator.ofFloat(currentText,
                                        "translationY", 0F, -progressBar.height.toFloat())
                                        .apply {
                                            repeatCount = Animation.INFINITE
                                            duration = 1000
                                        }
                        downAnimation =
                                ObjectAnimator.ofFloat(if (currentText == itemText) itemTextSecond else itemText, "translationY", progressBar.height.toFloat(), 0F)
                                        .apply {
                                            repeatCount = Animation.INFINITE
                                            duration = 1000
                                        }
                    }
                }
                set.duration = 1000
                set.addPauseListener(object : Animator.AnimatorPauseListener {
                    override fun onAnimationPause(animation: Animator?) {
                        currentText
                                .translationY = if (ScrollDirection.UP == scrollDirection) -progressBar.height.toFloat() else
                            progressBar.height.toFloat()

                        if (currentText != itemText) {
                            itemText
                                    .animate()
                                    .setDuration(500)
                                    .translationY(-0F)
                                    .start()
                        }
                        if (currentText != itemTextSecond) {
                            itemTextSecond
                                    .animate()
                                    .setDuration(500)
                                    .translationY(-0F)
                                    .start()
                        }
                    }

                    override fun onAnimationResume(animation: Animator?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })
                set.playTogether(upAnimation, downAnimation)
                set.start()
                set.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }


                    override fun onAnimationEnd(animation: Animator?) {
                        currentText.translationY =
                                if (ScrollDirection.UP == scrollDirection) progressBar.height.toFloat()
                                else -progressBar.height.toFloat()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        if (currentText == itemText) {
                            itemText.text = text
                            currentText = itemTextSecond
                        } else {
                            itemTextSecond.text = text
                            currentText = itemText
                        }
                    }
                })
            }
        } else {
            itemTextSecond.text = ""
            itemText.text = ""
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

    fun animationStop() {
        progressAnimation.pause()
        set.pause()
    }

    enum class ScrollDirection {
        DOWN,
        UP
    }
}