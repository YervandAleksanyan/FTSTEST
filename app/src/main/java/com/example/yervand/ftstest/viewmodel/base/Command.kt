package com.example.yervand.ftstest.viewmodel.base

import androidx.databinding.ObservableBoolean

interface Command {
    fun isEnabled(): ObservableBoolean

    fun execute(obj: Any?)
}