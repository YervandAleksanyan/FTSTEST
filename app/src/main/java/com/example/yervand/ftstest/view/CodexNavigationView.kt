package com.example.yervand.ftstest.view

import android.animation.*
import android.content.Context
import android.util.AttributeSet
import android.util.Log
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
import com.example.yervand.ftstest.utill.CodexNavigationItem
import kotlin.math.absoluteValue

class CodexNavigationView : LinearLayout {

    private lateinit var partIndicator: CodexNavigationItem

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
    }

    private fun initIndicators() {
        partIndicator = findViewById(R.id.part_indicator)
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


    companion object {
        @BindingAdapter("codexItems", "visiblePosition", "scrollState")
        @JvmStatic
        fun renderCodexItemPosition(view: CodexNavigationView, codexItems: ObservableArrayList<CodexEntity>, pos: Int, state: Int) {
            when (state) {
                RecyclerView.SCROLL_STATE_DRAGGING -> {
                    if (!codexItems.isEmpty() && pos != -1) {
                        val parentItems = view.getParentCodexItemsList(codexItems[pos])
                                .reversed()
                        view.partIndicator.setItemText(view.mapCodexItem(parentItems.getOrNull(0)))
                        view.partIndicator.setProgressValue(view.calculatePercentage(codexItems[pos]))
                    }
                }
                RecyclerView.SCROLL_STATE_IDLE -> {

                }
            }
        }
    }

    private fun calculatePercentage(item: CodexEntity?): Int? {
        val parentEntity = item?.Parent
        val childrens = parentEntity?.Children
        val childPosition = childrens?.indexOf(item)

        return ((childPosition?.toFloat()?.div(childrens.size.toFloat()))?.times(100))?.toInt()
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
}