package com.example.yervand.ftstest.viewmodel

import com.example.yervand.ftstest.db.model.CodexEntity
import com.example.yervand.ftstest.db.repository.realm.RealmDataRepository
import com.example.yervand.ftstest.db.repository.sqlite.FTSDataRepository
import com.example.yervand.ftstest.viewmodel.base.implementation.BaseAsyncCommand
import io.reactivex.Single

class SearchCommand constructor(private var viewModel: SearchViewModel, private var repository: FTSDataRepository, private var realmDataRepository: RealmDataRepository) : BaseAsyncCommand<List<CodexEntity>>() {


    override fun getAsyncAction(obj: Any?): Single<List<CodexEntity>>? {
        return repository.search(obj.toString())
    }

    override fun handleResult(result: List<CodexEntity>): Boolean {
        return if (!result.isEmpty()) {
            viewModel.codexItems?.clear()
            viewModel.codexItems?.addAll(result)
            true
        } else {
            if (viewModel.searchKey.get()?.isEmpty()!!) {
                viewModel.codexItems?.clear()
                viewModel.codexItems?.addAll(realmDataRepository.all())
            } else {
                viewModel.codexItems?.clear()
                failureMessage.set(NO_RESULTS)
            }
            false
        }
    }

    companion object {
        private const val NO_RESULTS = "No results"
    }
}