package br.com.bootcamp.magicgamecs.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bootcamp.magicgamecs.domain.FetchCollectionPage
import br.com.bootcamp.magicgamecs.models.pojo.State
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CollectionViewModel(
    private val fetchCollectionPage: FetchCollectionPage
) : ViewModel() {

    private val collections = MutableLiveData<List<CollectionItem>>()

    val initialLoadState = MutableLiveData<State>()
    val paginatedLoadState = MutableLiveData<State>()

    private var nextPage: Int? = null

    fun getItemsSet(): LiveData<List<CollectionItem>> {
        loadInitial()
        return collections
    }

    private fun loadInitial() {
        if (collections.value?.isNotEmpty() == false || initialLoadState.value == State.Loaded)
            return
        viewModelScope.launch {
            try {
                initialLoadState.postValue(State.Loading)
                val result = fetchPage()
                collections.postValue(result)
                initialLoadState.postValue(State.Loaded)
            } catch (e: Throwable) {
                initialLoadState.postValue(State.Failed(e))
            }
        }
    }

    fun fetchMoreItems() {
        val nextPage = this.nextPage ?: return
        if (paginatedLoadState.value == State.Loading) return

        viewModelScope.launch {
            try {
                paginatedLoadState.postValue(State.Loading)
                val result = fetchPage(nextPage)
                collections.postValue(result)
                paginatedLoadState.postValue(State.Loaded)
            } catch (e: Throwable) {
                paginatedLoadState.postValue(State.Failed(e))
            }
        }
    }

    private suspend fun fetchPage(page: Int = 0): List<CollectionItem> {
        val result = fetchCollectionPage(FetchCollectionPage.Params(page))
        this.nextPage = result.nextPage
        return result.data
            .flatMap { set ->
                listOf(NameCollectionItem(set.name)) +
                        set.typedCards.flatMap { type ->
                            listOf(CardTypeItem(set.code, type.type)) +
                                    type.cards.map { card -> CardItem(card) }
                        }
            }.let {
                if (result.nextPage != null) it + Placeholder
                else it
            }
    }

    override fun onCleared() {
        super.onCleared()
        if (viewModelScope.isActive)
            viewModelScope.cancel()
    }
}