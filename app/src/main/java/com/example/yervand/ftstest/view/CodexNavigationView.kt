package com.example.yervand.ftstest.view

import android.animation.*
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.recyclerview.widget.RecyclerView
import com.example.yervand.ftstest.R
import com.example.yervand.ftstest.db.model.CodexEntity
import com.example.yervand.ftstest.db.model.CodexEntityType
import kotlin.math.absoluteValue

class CodexNavigationView : LinearLayout {

    private lateinit var partIndicator: TextView
    private lateinit var sectionIndicator: TextView
    private lateinit var chapterIndicator: TextView
    private lateinit var articleIndicator: TextView
    private lateinit var articlePartIndicator: TextView
    private lateinit var paragraphIndicator: TextView
    private lateinit var subParagraphIndicator: TextView
    private lateinit var valueAnimator: ValueAnimator

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
        View.inflate(context, R.layout.codex_navigation_layout, this)
        initIndicators()
        initAnimation()
    }

    private fun initIndicators() {
        partIndicator = findViewById(R.id.part_indicator)
        sectionIndicator = findViewById(R.id.section_indicator)
        chapterIndicator = findViewById(R.id.chapter_indicator)
        articleIndicator = findViewById(R.id.article_indicator)
        articlePartIndicator = findViewById(R.id.article_part_indicator)
        paragraphIndicator = findViewById(R.id.paragraph_indicator)
        subParagraphIndicator = findViewById(R.id.sub_paragraph_indicator)
    }


    private fun getParentCodexItemsList(item: CodexEntity?): List<CodexEntity?> {
        var parentEntities = emptyList<CodexEntity?>()
        var currentEntity: CodexEntity? = item
        parentEntities += currentEntity

        while (currentEntity != null) {
            currentEntity = currentEntity.Parent
            currentEntity?.NumberString?.let {
                parentEntities += currentEntity
            }
        }
        return parentEntities
    }

    private fun parseType(type: Int?): String = when (type) {
        CodexEntityType.Part.value -> "Մ"
        CodexEntityType.Section.value -> "Բ"
        CodexEntityType.Chapter.value -> "Գ"
        CodexEntityType.Article.value -> "Հ"
        CodexEntityType.ArticlePart.value -> "Մ"
        CodexEntityType.Paragraph.value -> "Կ"
        CodexEntityType.SubParagraph.value -> "Ե"
        else -> {
            ""
        }
    }

    private fun mapCodexItem(codexEntity: CodexEntity?): String {
        return when {
            codexEntity != null -> "${parseType(codexEntity.Type)}${codexEntity.NumberString ?: ""}"
            else -> ""
        }

    }

    companion object {
        @BindingAdapter("codexItems", "visiblePosition", "scrollState", "rotateValue")
        @JvmStatic
        fun renderCodexItemPosition(view: CodexNavigationView, codexItems: ObservableArrayList<CodexEntity>, pos: Int, state: Int, rotateValue: Float) {
            when (state) {
                RecyclerView.SCROLL_STATE_DRAGGING -> {
                    if (!codexItems.isEmpty() && pos != -1) {
                        val parentItems = view.getParentCodexItemsList(codexItems[pos])
                                .reversed()
                        view.partIndicator
                                .rotationX = ((360 * rotateValue) / 100)
                        when (rotateValue) {
                            in 50..180 -> view.partIndicator.alpha = (rotateValue / 1000)
                            in 180F..310F -> view.partIndicator.alpha = (rotateValue) / 4000
                            else -> view.partIndicator.alpha = 1F
                        }
//                        view.valueAnimator.setValues(PropertyValuesHolder.ofFloat("rotationX", (360 * rotateValue) / 100))
//                        view.startAnimation()
                        if (parentItems.size > 1 && !view.mapCodexItem(parentItems.getOrNull(0)).isEmpty()) {
                            view.indicatorVisibility(view.partIndicator, view.mapCodexItem(parentItems.getOrNull(0)))
                            view.indicatorVisibility(view.sectionIndicator, view.mapCodexItem(parentItems.getOrNull(1)))
                            view.indicatorVisibility(view.chapterIndicator, view.mapCodexItem(parentItems.getOrNull(2)))
                            view.indicatorVisibility(view.articleIndicator, view.mapCodexItem(parentItems.getOrNull(3)))
                            view.indicatorVisibility(view.articlePartIndicator, view.mapCodexItem(parentItems.getOrNull(4)))
                            view.indicatorVisibility(view.paragraphIndicator, view.mapCodexItem(parentItems.getOrNull(5)))
                            view.indicatorVisibility(view.subParagraphIndicator, view.mapCodexItem(parentItems.getOrNull(6)))
                        } else {
                            view.visiblyAllIndicators()
                        }
                        view.partIndicator.text = view.mapCodexItem(parentItems.getOrNull(0))
                        view.sectionIndicator.text = view.mapCodexItem(parentItems.getOrNull(1))
                        view.chapterIndicator.text = view.mapCodexItem(parentItems.getOrNull(2))
                        view.articleIndicator.text = view.mapCodexItem(parentItems.getOrNull(3))
                        view.articlePartIndicator.text = view.mapCodexItem(parentItems.getOrNull(4))
                        view.paragraphIndicator.text = view.mapCodexItem(parentItems.getOrNull(5))
                        view.subParagraphIndicator.text = view.mapCodexItem(parentItems.getOrNull(6))
                    }
                }

                RecyclerView.SCROLL_STATE_IDLE -> {
                    view.partIndicator.animate()
                            .setDuration(500)
                            .rotationX(0.0F)
                            .alpha(1.0F)
                            .start()
                }
            }
        }
    }


    private fun initAnimation() {
        valueAnimator = ValueAnimator.ofFloat(0.0F, 360f)
        valueAnimator.addUpdateListener {
            when {
                it.animatedValue as Float in 50F..180F -> {
                    partIndicator.alpha = (it.animatedValue as Float) / 100
                    sectionIndicator.alpha = (it.animatedValue as Float) / 100
                    chapterIndicator.alpha = (it.animatedValue as Float) / 100
                    articleIndicator.alpha = (it.animatedValue as Float) / 100
                    articlePartIndicator.alpha = (it.animatedValue as Float) / 100
                    paragraphIndicator.alpha = (it.animatedValue as Float) / 100
                    subParagraphIndicator.alpha = (it.animatedValue as Float) / 100
                }
                it.animatedValue as Float in 180F..310F -> {
                    partIndicator.alpha = (it.animatedValue as Float) / 400
                    sectionIndicator.alpha = (it.animatedValue as Float) / 400
                    chapterIndicator.alpha = (it.animatedValue as Float) / 400
                    articleIndicator.alpha = (it.animatedValue as Float) / 400
                    articlePartIndicator.alpha = (it.animatedValue as Float) / 400
                    paragraphIndicator.alpha = (it.animatedValue as Float) / 400
                    subParagraphIndicator.alpha = (it.animatedValue as Float) / 400
                }
                else -> {
                    partIndicator.alpha = 1F
                    sectionIndicator.alpha = 1F
                    chapterIndicator.alpha = 1F
                    articleIndicator.alpha = 1F
                    articlePartIndicator.alpha = 1F
                    paragraphIndicator.alpha = 1F
                    subParagraphIndicator.alpha = 1F
                }
            }
            partIndicator.rotationX = it.animatedValue as Float
            sectionIndicator.rotationX = it.animatedValue as Float
            chapterIndicator.rotationX = it.animatedValue as Float
            articleIndicator.rotationX = it.animatedValue as Float
            articlePartIndicator.rotationX = it.animatedValue as Float
            paragraphIndicator.rotationX = it.animatedValue as Float
            subParagraphIndicator.rotationX = it.animatedValue as Float
        }
        valueAnimator.addPauseListener(object : Animator.AnimatorPauseListener {
            override fun onAnimationResume(animation: Animator?) {

            }

            override fun onAnimationPause(animation: Animator?) {
                partIndicator.animate()
                        .setDuration(1000)
                        .rotationX(0.0F)
                        .alpha(1.0F)
                        .start()

                sectionIndicator.animate()
                        .setDuration(1000)
                        .rotationX(0.0F)
                        .alpha(1.0F)
                        .start()
                chapterIndicator.animate()
                        .setDuration(1000)
                        .rotationX(0.0F)
                        .alpha(1.0F)
                        .start()
                articleIndicator.animate()
                        .setDuration(1000)
                        .rotationX(0.0F)
                        .alpha(1.0F)
                        .start()
                articlePartIndicator.animate()
                        .setDuration(1000)
                        .rotationX(0.0F)
                        .alpha(1.0F)
                        .start()
                paragraphIndicator.animate()
                        .setDuration(1000)
                        .rotationX(0.0F)
                        .alpha(1.0F)
                        .start()
                subParagraphIndicator.animate()
                        .setDuration(1000)
                        .rotationX(0.0F)
                        .alpha(1.0F)
                        .start()

            }
        })
        valueAnimator.start()
        valueAnimator.pause()
    }

    private fun startAnimation() {
        valueAnimator.resume()
    }


    private fun stopAnimation() {
        valueAnimator.pause()
    }

    private fun indicatorVisibility(view: View, content: String) {
        if (content.isEmpty()) view.visibility = View.INVISIBLE
        else view.visibility = View.VISIBLE
    }

    private fun visiblyAllIndicators() {
        partIndicator.visibility = View.VISIBLE
        sectionIndicator.visibility = View.VISIBLE
        chapterIndicator.visibility = View.VISIBLE
        articleIndicator.visibility = View.VISIBLE
        articlePartIndicator.visibility = View.VISIBLE
        paragraphIndicator.visibility = View.VISIBLE
        subParagraphIndicator.visibility = View.VISIBLE
    }

    override fun onViewRemoved(child: View?) {
        super.onViewRemoved(child)
        valueAnimator.cancel()
    }
}