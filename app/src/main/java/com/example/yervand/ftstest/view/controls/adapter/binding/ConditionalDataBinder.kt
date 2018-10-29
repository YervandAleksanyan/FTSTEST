package com.example.yervand.ftstest.view.controls.adapter.binding

abstract class ConditionalDataBinder<T>(bindingVariable: Int, layoutId: Int)
    : ItemBinderBase<T>(bindingVariable, layoutId) {

    abstract fun canHandle(model: T): Boolean

    fun canHandle(layoutId: Int): Boolean = this.layoutId == layoutId
}