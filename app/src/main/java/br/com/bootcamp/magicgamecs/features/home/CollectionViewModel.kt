package br.com.bootcamp.magicgamecs.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bootcamp.magicgamecs.domain.FetchCollectionPage
import br.com.bootcamp.magicgamecs.models.pojo.ViewState
import br.com.bootcamp.magicgamecs.models.pojo.ViewState.Loading
import br.com.bootcamp.magicgamecs.models.pojo.ViewState.Success
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

typealias CollectionsState = ViewState<List<CollectionItem>>

class CollectionViewModel(
    private val fetchCollectionPage: FetchCollectionPage,
    initialState: CollectionsState = ViewState.FirstLaunch
) : ViewModel() {

    private val collections = MutableLiveData<CollectionsState>()
        .apply { value = initialState }

    private var nextPage: Int? = null

    fun getCollections(): LiveData<CollectionsState> {
        loadInitial()
        return collections
    }

    fun reload() {
        if (collections.value is Loading.FromEmpty) return

        viewModelScope.launch {
            try {
                collections.postValue(Loading.FromEmpty)
                val result = fetchPage()
                collections.postValue(Success(result))
            } catch (e: Throwable) {
                collections.postValue(ViewState.Failed.FromEmpty(e))
            }
        }
    }

    fun loadInitial() {
        if (collections.value !is ViewState.FirstLaunch && collections.value !is ViewState.Failed.FromEmpty)
            return

        viewModelScope.launch {
            try {
                collections.postValue(Loading.FromEmpty)
                val result = fetchPage()
                collections.postValue(Success(result))
            } catch (e: Throwable) {
                collections.postValue(ViewState.Failed.FromEmpty(e))
            }
        }
    }

    fun fetchMoreItems() {
        val nextPage = this.nextPage ?: return
        val previous = (collections.value as? Success)?.value ?: return

        viewModelScope.launch {
            try {
                collections.postValue(Loading.FromPrevious(previous))
                val result = fetchPage(nextPage)
                collections.postValue(Success(result))
            } catch (e: Throwable) {
                collections.postValue(ViewState.Failed.FromPrevious(e, previous))
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

    override fun onCleared() {
        super.onCleared()
        if (viewModelScope.isActive)
            viewModelScope.cancel()
    }
}