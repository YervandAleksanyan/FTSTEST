package com.example.yervand.ftstest.view

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Scroller
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yervand.ftstest.BR
import com.example.yervand.ftstest.R
import com.example.yervand.ftstest.databinding.ActivitySearchBinding
import com.example.yervand.ftstest.db.model.CodexEntity
import com.example.yervand.ftstest.di.factories.SearchViewModelFactory
import com.example.yervand.ftstest.view.base.BaseActivity
import com.example.yervand.ftstest.view.controls.adapter.binding.ItemBinderBase
import com.example.yervand.ftstest.viewmodel.SearchViewModel
import javax.inject.Inject


class SearchActivity : BaseActivity() {

    @Inject
    lateinit var factory: SearchViewModelFactory

    private lateinit var binding: ActivitySearchBinding

    private lateinit var viewModel: SearchViewModel

    private lateinit var layoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
    }

    private fun initBindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        viewModel = ViewModelProviders.of(this, factory)[SearchViewModel::class.java]
        binding.viewModel = viewModel
        binding.view = this
        layoutManager = LinearLayoutManager(this)
        binding.itemsRv.layoutManager = layoutManager
        binding.itemsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val position = layoutManager.findFirstVisibleItemPosition()
                if (dy != 0) {
                    viewModel.scrollState.set(RecyclerView.SCROLL_STATE_DRAGGING)
                    viewModel.scrollPosition.set(position)
                    viewModel.scrollDirection.set(if (dy > 0) 0 else 1)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.scrollState.set(RecyclerView.SCROLL_STATE_IDLE)
                }
            }
        })
    }

    fun getViewBinder(): ItemBinderBase<CodexEntity> = ItemBinderBase(BR.item, R.layout.codex_item)
}
