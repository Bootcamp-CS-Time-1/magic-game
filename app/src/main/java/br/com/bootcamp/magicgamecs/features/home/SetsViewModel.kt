package br.com.bootcamp.magicgamecs.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bootcamp.magicgamecs.domain.FetchMagicSetsPage
import br.com.bootcamp.magicgamecs.models.pojo.State
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SetsViewModel(
    private val fetchMagicSetsPage: FetchMagicSetsPage
) : ViewModel() {
    private val itemsSet = MutableLiveData<List<ItemSet>>()

    val initialLoadState = MutableLiveData<State>()
    val paginatedLoadState = MutableLiveData<State>()

    private var nextPage: Int? = null

    fun getItemsSet(): LiveData<List<ItemSet>> {
        loadInitial()
        return itemsSet
    }

    private fun loadInitial() {
        if (itemsSet.value?.isNotEmpty() == false || initialLoadState.value == State.Loaded)
            return
        viewModelScope.launch {
            try {
                initialLoadState.postValue(State.Loading)
                val result = fetchPage()
                itemsSet.postValue(result)
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
                itemsSet.postValue(result)
                paginatedLoadState.postValue(State.Loaded)
            } catch (e: Throwable) {
                paginatedLoadState.postValue(State.Failed(e))
            }
        }
    }

    private suspend fun fetchPage(page: Int = 0): List<ItemSet> {
        val result = fetchMagicSetsPage(FetchMagicSetsPage.Params(page))
        this.nextPage = result.nextPage
        return result.data
            .flatMap { set ->
                listOf(EditionItemSet(set.name)) +
                        set.typedCards.flatMap { type ->
                            listOf(TypeItemSet(set.code, type.type)) +
                                    type.cards.map { card -> CardItemSet(card) }
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