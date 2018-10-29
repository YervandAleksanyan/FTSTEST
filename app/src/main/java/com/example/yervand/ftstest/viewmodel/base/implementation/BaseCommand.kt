package com.example.yervand.ftstest.viewmodel.base.implementation

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import com.example.yervand.ftstest.viewmodel.base.Command

abstract class BaseCommand : BaseObservable(), Command {
    var enabled: ObservableBoolean = ObservableBoolean()

    init {
        enabled.set(true)
    }

    override fun isEnabled(): ObservableBoolean = enabled
}