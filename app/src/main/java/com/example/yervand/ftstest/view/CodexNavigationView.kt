package com.example.yervand.ftstest.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.example.yervand.ftstest.R
import com.example.yervand.ftstest.db.model.CodexEntity
import com.example.yervand.ftstest.db.model.CodexEntityType
import com.example.yervand.ftstest.utill.CodexNavigationItem

class CodexNavigationView : LinearLayout {

    private lateinit var partIndicator: CodexNavigationItem
    private lateinit var sectionIndicator: CodexNavigationItem
    private lateinit var chapterIndicator: CodexNavigationItem
    private lateinit var articleIndicator: CodexNavigationItem
    private lateinit var articlePartIndicator: CodexNavigationItem
    private lateinit var paragraphIndicator: CodexNavigationItem
    private lateinit var subParagraphIndicator: CodexNavigationItem

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
        sectionIndicator = findViewById(R.id.section_indicator)
        chapterIndicator = findViewById(R.id.chapter_indicator)
        articleIndicator = findViewById(R.id.article_indicator)
        articlePartIndicator = findViewById(R.id.article_part_indicator)
        paragraphIndicator = findViewById(R.id.paragraph_indicator)
        subParagraphIndicator = findViewById(R.id.sub_paragraph_indicator
        )
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
        @BindingAdapter("codexItems", "visiblePosition", "scrollState", "scrollDirection")
        @JvmStatic
        fun renderCodexItemPosition(view: CodexNavigationView, codexItems: ObservableArrayList<CodexEntity>, pos: Int, state: Int, direction: Int) {
            when (state) {
                RecyclerView.SCROLL_STATE_DRAGGING -> {
                    if (!codexItems.isEmpty() && pos != -1) {
                        view.partIndicator.setProgressValue(view.calculatePercentage(codexItems[pos]))
                        view.sectionIndicator.setProgressValue(view.calculatePercentage(codexItems[pos]))
                        view.chapterIndicator.setProgressValue(view.calculatePercentage(codexItems[pos]))
                        view.articleIndicator.setProgressValue(view.calculatePercentage(codexItems[pos]))
                        view.articlePartIndicator.setProgressValue(view.calculatePercentage(codexItems[pos]))
                        view.paragraphIndicator.setProgressValue(view.calculatePercentage(codexItems[pos]))
                        view.subParagraphIndicator.setProgressValue(view.calculatePercentage(codexItems[pos]))
                    }
                }
                RecyclerView.SCROLL_STATE_IDLE -> {
                    val parentItems = view.getParentCodexItemsList(codexItems[pos])
                            .reversed()
                    val scrollDirection: CodexNavigationItem.ScrollDirection =
                            if (direction == 0) CodexNavigationItem.ScrollDirection.DOWN
                            else CodexNavigationItem.ScrollDirection.UP

                    if (!parentItems.isEmpty()) {
                        view.partIndicator.setItemText(view.mapCodexItem(parentItems.getOrNull(0)), scrollDirection)
                        view.sectionIndicator.setItemText(view.mapCodexItem(parentItems.getOrNull(1)), scrollDirection)
                        view.chapterIndicator.setItemText(view.mapCodexItem(parentItems.getOrNull(2)), scrollDirection)
                        view.articleIndicator.setItemText(view.mapCodexItem(parentItems.getOrNull(3)), scrollDirection)
                        view.articlePartIndicator.setItemText(view.mapCodexItem(parentItems.getOrNull(4)), scrollDirection)
                        view.paragraphIndicator.setItemText(view.mapCodexItem(parentItems.getOrNull(5)), scrollDirection)
                        view.subParagraphIndicator.setItemText(view.mapCodexItem(parentItems.getOrNull(6)), scrollDirection)

                        view.partIndicator.progressAnimationStop()
                        view.sectionIndicator.progressAnimationStop()
                        view.chapterIndicator.progressAnimationStop()
                        view.articleIndicator.progressAnimationStop()
                        view.articlePartIndicator.progressAnimationStop()
                        view.paragraphIndicator.progressAnimationStop()
                        view.subParagraphIndicator.progressAnimationStop()
                    }
                }
            }
        }
    }

    private fun calculatePercentage(item: CodexEntity?): Int? {
        val parentEntity = item?.Parent
        val children = parentEntity?.Children
        val childPosition = children?.indexOf(item)

        return ((childPosition?.toFloat()?.div(children.size.toFloat()))?.times(100))?.toInt()
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