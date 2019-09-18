package br.com.bootcamp.magicgamecs.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

class SetsViewModel(
    private val setsDataSourceFactory: SetsDataSourceFactory
) : ViewModel() {


    val itemsSet: LiveData<PagedList<ItemSet>> by lazy {
        initializePagedList().build()
    }

    val initialLoadState: LiveData<State>
        get() = setsDataSourceFactory.initialLoadState

    val paginatedLoadState: LiveData<State>
        get() = setsDataSourceFactory.paginatedLoadState

    private fun initializePagedList(): LivePagedListBuilder<Int, ItemSet> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .build()
        return LivePagedListBuilder<Int, ItemSet>(setsDataSourceFactory, config)
    }
}