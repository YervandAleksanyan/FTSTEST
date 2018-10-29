package com.example.yervand.ftstest.view.controls.adapter.binding

interface ItemBinder<T> {
    fun getLayoutRes(model: T): Int

    fun getBindingVariable(model: T): Int
}