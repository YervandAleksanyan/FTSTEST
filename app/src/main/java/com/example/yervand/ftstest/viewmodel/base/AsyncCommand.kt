package com.example.yervand.ftstest.viewmodel.base

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

interface AsyncCommand : DisposableCommand {
    fun isBusy(): ObservableBoolean

    fun isFinished(): ObservableBoolean

    fun failureMessage(): ObservableField<String>
}