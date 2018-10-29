package com.example.yervand.ftstest.viewmodel.base.implementation

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.example.yervand.ftstest.viewmodel.base.AsyncCommand
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

abstract class BaseAsyncCommand<T> : BaseCommand(), AsyncCommand, Disposable {
    var busy: ObservableBoolean = ObservableBoolean()

    var finished: ObservableBoolean = ObservableBoolean()

    var failureMessage: ObservableField<String> = ObservableField()

    init {
        busy.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                enabled.set(!busy.get())
            }
        })
    }

    private var action: Disposable? = null
    @Volatile
    private var disposed: Boolean = false

    override fun execute(obj: Any?) {
        if (!enabled.get()) return
        val task = getAsyncAction(obj)
        task?.let {
                busy.set(true)
                finished.set(false)
                failureMessage.set("")
                action = it
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith<DisposableSingleObserver<T>>(getAsyncActionObserver())
        }
    }

    protected abstract fun getAsyncAction(obj: Any?): Single<T>?

    protected abstract fun handleResult(result: T): Boolean

    protected open fun handleError(e: Throwable) {
        failureMessage.set("Something went wrong while executing the action. Please try again")
    }

    override fun dispose() {
        disposeAction()
        disposed = true
    }

    override fun isDisposed(): Boolean = disposed

    private fun getAsyncActionObserver(): DisposableSingleObserver<T> {
        return object : DisposableSingleObserver<T>() {
            override fun onSuccess(result: T) {
                busy.set(false)
                finished.set(handleResult(result))
                disposeAction()
            }

            override fun onError(e: Throwable) {
                busy.set(false)
                finished.set(true)
                handleError(e)
                disposeAction()
            }
        }
    }

    private fun disposeAction() {
        action?.let {
            it.dispose()
            action = null
        }
    }

    override fun isBusy(): ObservableBoolean = busy

    override fun isFinished(): ObservableBoolean = finished

    override fun failureMessage(): ObservableField<String> = failureMessage
}


