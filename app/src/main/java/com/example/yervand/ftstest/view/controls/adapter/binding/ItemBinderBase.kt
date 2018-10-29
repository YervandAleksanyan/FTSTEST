package com.example.yervand.ftstest.view.controls.adapter.binding

open class ItemBinderBase<T>(protected val bindingVariable: Int,
                             protected val layoutId: Int) : ItemBinder<T> {

    override fun getLayoutRes(model: T): Int = layoutId

    override fun getBindingVariable(model: T): Int = bindingVariable
}