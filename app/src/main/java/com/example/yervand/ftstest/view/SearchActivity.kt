package com.example.yervand.ftstest.view

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

interface IViewModelProvider {
    fun getSearchViewModel(context: AppCompatActivity): SearchViewModel
}

class AppViewModelProvider @Inject constructor(var factory: SearchViewModelFactory) : IViewModelProvider {
    override fun getSearchViewModel(context: AppCompatActivity): SearchViewModel {
        return ViewModelProviders.of(context, factory)[SearchViewModel::class.java]
    }
}

class SearchActivity : BaseActivity() {

    @Inject
    lateinit var viewModelProvider: IViewModelProvider

    private lateinit var binding: ActivitySearchBinding

    private lateinit var viewModel: SearchViewModel

    private lateinit var layoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
    }

    private fun initBindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        viewModel = viewModelProvider.getSearchViewModel(this)
        binding.viewModel = viewModel
        binding.view = this
        layoutManager = LinearLayoutManager(this)
        binding.itemsRv.layoutManager = layoutManager
        var position = 0
        var dY = 0
        binding.itemsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                position = layoutManager.findFirstVisibleItemPosition()
                if (dy != 0) {
                    viewModel.scrollState.set(RecyclerView.SCROLL_STATE_DRAGGING)
                    viewModel.scrollPosition.set(position)
                    dY = dy
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.scrollState.set(RecyclerView.SCROLL_STATE_IDLE)
                    viewModel.scrollPosition.set(position)
                    viewModel.scrollDirection.set(if (dY > 0) 0 else 1)
                }
            }
        })
    }

    fun getViewBinder(): ItemBinderBase<CodexEntity> = ItemBinderBase(BR.item, R.layout.codex_item)
}
