package br.com.bootcamp.magicgamecs.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bootcamp.magicgamecs.domain.FetchCollectionPage
import br.com.bootcamp.magicgamecs.models.pojo.ViewState
import br.com.bootcamp.magicgamecs.models.pojo.ViewState.Loading
import br.com.bootcamp.magicgamecs.models.pojo.ViewState.Success
import kotlinx.coroutines.launch

typealias CollectionsState = ViewState<List<CollectionItem>>

class CollectionViewModel(
    private val fetchCollectionPage: FetchCollectionPage,
    initialState: CollectionsState = ViewState.FirstLaunch,
    _nextPage: Int? = null
) : ViewModel() {

    private val state = MutableLiveData<CollectionsState>()
        .apply { value = initialState }

    val collectionsState: LiveData<CollectionsState>
        get() = state

    private var nextPage: Int? = _nextPage

    fun reload() {
        if (state.value is Loading.FromEmpty) return
        load()
    }

    fun loadInitial() {
        if (state.value !is ViewState.FirstLaunch && state.value !is ViewState.Failed.FromEmpty)
            return
        load()
    }

    private fun load() {
        viewModelScope.launch {
            try {
                state.postValue(Loading.FromEmpty)
                val result = fetchPage()
                state.postValue(Success(result))
            } catch (e: Throwable) {
                e.printStackTrace()
                state.postValue(ViewState.Failed.FromEmpty(e))
            }
        }
    }

    fun fetchMoreItems() {
        val nextPage = this.nextPage ?: return
        val previous = (state.value as? Success)?.value
            ?: (state.value as? ViewState.Failed.FromPrevious)?.previous
            ?: return

        viewModelScope.launch {
            try {
                state.postValue(Loading.FromPrevious(previous))
                val result = fetchPage(nextPage)
                state.postValue(Success(result))
            } catch (e: Throwable) {
                e.printStackTrace()
                state.postValue(ViewState.Failed.FromPrevious(e, previous))
            }
        }
    }

    private suspend fun fetchPage(page: Int = 0): List<CollectionItem> {
        val result = fetchCollectionPage(FetchCollectionPage.Params(page))
        nextPage = result.nextPage
        return result.data
            .flatMap { collection ->
                listOf(NameCollectionItem(collection.name)) +
                        collection.typedCards.flatMap { type ->
                            listOf(CardTypeItem(collection.code, type.type)) +
                                    type.cards.map { card -> CardItem(card) }
                        }
            }
    }
}