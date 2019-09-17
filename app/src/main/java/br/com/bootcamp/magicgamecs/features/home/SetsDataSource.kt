package br.com.bootcamp.magicgamecs.features.home

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import br.com.bootcamp.magicgamecs.domain.LoadMagicSetsByPage
import kotlinx.coroutines.*

class SetsDataSourceFactory(
    private val loadMagicSetsByPage: LoadMagicSetsByPage
) : DataSource.Factory<Int, ItemSet>() {
    override fun create() =
        SetsDataSource(loadMagicSetsByPage)
}

class SetsDataSource(
    private val loadMagicSetsByPage: LoadMagicSetsByPage,
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
                callback.onResult(loadPage(1), null, 2)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ItemSet>) {
        ioScope.launch {
            try {
                callback.onResult(loadPage(params.key), params.key + 1)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ItemSet>) {
    }

    private suspend fun loadPage(page: Int): List<ItemSet> =
        loadMagicSetsByPage(LoadMagicSetsByPage.Params(page))
            .flatMap {
                listOf<ItemSet>(TitleItemSet(it.name)) + it.categorizedCards.flatMap { category ->
                    listOf<ItemSet>(SubtitleItemSet(category.type)) + category.cards.map { card ->
                        CardItemSet(card.id, card.image)
                    }
                }
            }

}