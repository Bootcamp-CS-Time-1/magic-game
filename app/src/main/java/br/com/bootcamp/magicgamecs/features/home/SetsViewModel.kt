package br.com.bootcamp.magicgamecs.features.home

import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

class SetsViewModel(
    private val setsDataSourceFactory: SetsDataSourceFactory
) : ViewModel() {

    val state = MutableLiveData<State>()
    val itemsSet: LiveData<PagedList<ItemSet>> by lazy {
        initializePagedList().build()
    }

    private fun initializePagedList(): LivePagedListBuilder<Int, ItemSet> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .build()
        return LivePagedListBuilder<Int, ItemSet>(setsDataSourceFactory, config)
    }

    sealed class State {
        object Loading : State()
        object Loaded : State()
        object Completed : State()
        data class Failed(val error: ErrorData) : State()
    }
}

data class ErrorData(val message: String, @DrawableRes val icon: Int)