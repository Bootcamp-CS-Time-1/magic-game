package br.com.bootcamp.magicgamecs.features.home

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import br.com.bootcamp.magicgamecs.domain.LoadMagicSetsByPage
import kotlinx.coroutines.*

class SetsDataSourceFactory(
    private val loadMagicSetsByPage: LoadMagicSetsByPage
) : DataSource.Factory<Int, ItemSet>() {

    val initialLoadState = MutableLiveData<State>()
    val paginatedLoadState = MutableLiveData<State>()

    override fun create() =
        SetsDataSource(loadMagicSetsByPage, initialLoadState, paginatedLoadState)
}

class SetsDataSource(
    private val loadMagicSetsByPage: LoadMagicSetsByPage,
    private val initialLoadState: MutableLiveData<State>,
    private val paginatedLoadState: MutableLiveData<State>,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PageKeyedDataSource<Int, ItemSet>() {

    private val job = SupervisorJob()
    private val ioScope = CoroutineScope(ioDispatcher + job)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ItemSet>
    ) {
        ioScope.launch {
            try {
                initialLoadState.postValue(State.Loading)
                callback.onResult(loadPage(1), null, 2)
                initialLoadState.postValue(State.Loaded)
            } catch (e: Exception) {
                initialLoadState.postValue(State.Failed(e))
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ItemSet>) {
        ioScope.launch {
            try {
                paginatedLoadState.postValue(State.Loading)
                val result = loadPage(params.key)
                callback.onResult(result, params.key + 1)
                paginatedLoadState.postValue(State.Loaded)
            } catch (e: Exception) {
                paginatedLoadState.postValue(State.Failed(e))
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ItemSet>) {
    }

    private suspend fun loadPage(page: Int): List<ItemSet> =
        loadMagicSetsByPage(LoadMagicSetsByPage.Params(page))
            .flatMap { set ->
                listOf(EditionItemSet(set.name)) +
                        set.cardTypes.flatMap { type ->
                            listOf(TypeItemSet(type.type)) +
                                    type.cards.map { card -> CardItemSet(card) }
                        }
            }
}

sealed class State {
    object Loading : State()
    object Loaded : State()
    data class Failed(val error: Throwable) : State()
}