package com.example.yervand.ftstest.view.controls

import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.example.yervand.ftstest.db.model.CodexEntity
import com.example.yervand.ftstest.view.controls.adapter.BindingRecyclerViewAdapter
import com.example.yervand.ftstest.view.controls.adapter.ClickHandler
import com.example.yervand.ftstest.view.controls.adapter.LongClickHandler
import com.example.yervand.ftstest.view.controls.adapter.binding.ItemBinder
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.text.Spannable
import android.graphics.Typeface
import android.text.style.TextAppearanceSpan
import android.content.res.ColorStateList
import android.text.SpannableString



private const val KEY_ITEMS = -123
private const val KEY_CLICK_HANDLER = -124
private const val KEY_LONG_CLICK_HANDLER = -125

@BindingAdapter("items")
fun <T> setItems(recyclerView: RecyclerView, items: Collection<T>) {
    val adapter = recyclerView.adapter as? BindingRecyclerViewAdapter<T>
    adapter?.setItems(items) ?: recyclerView.setTag(KEY_ITEMS, items)
}

@BindingAdapter("clickHandler")
fun <T> setHandler(recyclerView: RecyclerView, handler: ClickHandler<T>) {
    val adapter = recyclerView.adapter as? BindingRecyclerViewAdapter<T>
    if (adapter != null) {
        adapter.clickHandler = handler
    } else {
        recyclerView.setTag(KEY_CLICK_HANDLER, handler)
    }
}

@BindingAdapter("longClickHandler")
fun <T> setHandler(recyclerView: RecyclerView, handler: LongClickHandler<T>) {
    val adapter = recyclerView.adapter as? BindingRecyclerViewAdapter<T>
    if (adapter != null) {
        adapter.longClickHandler = handler
    } else {
        recyclerView.setTag(KEY_LONG_CLICK_HANDLER, handler)
    }
}

@BindingAdapter("itemViewBinder")
fun <T> setItemViewBinder(recyclerView: RecyclerView, itemViewMapper: ItemBinder<T>) {
    val items = recyclerView.getTag(KEY_ITEMS) as? ObservableList<T>
    val clickHandler = recyclerView.getTag(KEY_CLICK_HANDLER) as? ClickHandler<T>
    val adapter = BindingRecyclerViewAdapter(itemViewMapper, items)
    if (clickHandler != null) {
        adapter.clickHandler = clickHandler
    }
    recyclerView.adapter = adapter
}



