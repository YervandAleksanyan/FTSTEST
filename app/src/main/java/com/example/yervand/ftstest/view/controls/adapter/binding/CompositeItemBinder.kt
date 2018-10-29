package com.example.yervand.ftstest.view.controls.adapter.binding

import com.example.yervand.ftstest.view.controls.adapter.binding.ItemBinder

class CompositeItemBinder<T>(private var conditionalDataBinders: Array<ConditionalDataBinder<T>>) : ItemBinder<T> {


    override fun getLayoutRes(model: T): Int {
        conditionalDataBinders.indices.forEach {
            val dataBinder = conditionalDataBinders[it]
            if (dataBinder.canHandle(model))
                return dataBinder.getLayoutRes(model)
        }

        throw IllegalStateException()
    }

    override fun getBindingVariable(model: T): Int {
        conditionalDataBinders.indices.forEach {
            val dataBinder = conditionalDataBinders[it]
            if (dataBinder.canHandle(model))
                return dataBinder.getBindingVariable(model)
        }

        throw IllegalStateException()
    }
}