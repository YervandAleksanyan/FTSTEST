package com.example.yervand.ftstest.viewmodel

import androidx.databinding.Observable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.yervand.ftstest.db.model.CodexEntity
import com.example.yervand.ftstest.db.repository.realm.RealmDataRepository
import com.example.yervand.ftstest.db.repository.sqlite.FTSDataRepository
import javax.inject.Inject
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch


class SearchViewModel @Inject constructor(repository: FTSDataRepository, realmRepository: RealmDataRepository) : ViewModel() {

    var codexItems: ObservableArrayList<CodexEntity>? = null
    val searchKey: ObservableField<String> = ObservableField()
    val searchCommand = SearchCommand(this, repository, realmRepository)
    var scrollPosition: ObservableField<Int> = ObservableField()
    var scrollState: ObservableField<Int> = ObservableField()

    var typing = Job()
    private var searchKeyChangeCallback: Observable.OnPropertyChangedCallback

    init {
        codexItems = ObservableArrayList()
        val dataInitialCommand = DataInitialCommand(this, repository, realmRepository)
        dataInitialCommand.execute(null)
        searchKeyChangeCallback = (object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (typing.isActive)
                    typing.cancel()

                typing = launch(UI) {
                    delay(500)
                    val searchQuery = sender as? ObservableField<*>
                    if (searchQuery?.get().toString().length > 3 ||
                            searchQuery?.get().toString().isEmpty()) searchCommand.execute(searchQuery?.get())
                }
            }
        })
        searchKey.addOnPropertyChangedCallback(searchKeyChangeCallback)
    }

    override fun onCleared() {
        super.onCleared()
        typing.cancel()
        searchKey.removeOnPropertyChangedCallback(searchKeyChangeCallback)
    }
}