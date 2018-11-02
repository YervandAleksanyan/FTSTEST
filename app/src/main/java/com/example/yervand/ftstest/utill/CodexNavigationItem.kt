package com.example.yervand.ftstest.utill

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.yervand.ftstest.R

class CodexNavigationItem : FrameLayout {

    private lateinit var progressBar: ProgressBar
    private lateinit var itemText: TextView

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
    }

    fun setProgressValue(value: Int?) {
        if (value in 0..100) progressBar.progress = value ?:0
    }

    fun setItemText(text: String) {
        itemText.text = text
    }
}