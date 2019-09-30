package br.com.bootcamp.magicgamecs.features.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bootcamp.magicgamecs.domain.FetchFavoriteCards
import br.com.bootcamp.magicgamecs.features.home.CardItem
import br.com.bootcamp.magicgamecs.features.home.CardTypeItem
import br.com.bootcamp.magicgamecs.features.home.CollectionItem
import br.com.bootcamp.magicgamecs.features.home.NameCollectionItem
import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.Collection
import br.com.bootcamp.magicgamecs.models.pojo.ViewState
import br.com.bootcamp.magicgamecs.models.pojo.ViewState.Loading
import br.com.bootcamp.magicgamecs.models.pojo.ViewState.Success
import kotlinx.coroutines.launch

typealias CollectionsState = ViewState<List<CollectionItem>>

class FavoritesViewModel(
    private val fetchFavoriteCards: FetchFavoriteCards,
    initialState: CollectionsState = ViewState.FirstLaunch
) : ViewModel() {

    private val state = MediatorLiveData<CollectionsState>()
        .apply { value = initialState }

    val collectionsState: LiveData<CollectionsState>
        get() = state

    fun loadInitial() {
        if (state.value !is ViewState.FirstLaunch && state.value !is ViewState.Failed.FromEmpty)
            return
        load()
    }

    private fun load() {
        state.value = Loading.FromEmpty
        state.addSource(fetchFavoriteCards()) {
            state.value = if (it.isNotEmpty()) Success(transformData(it))
                else ViewState.Empty
        }
    }

    private fun transformData(data: List<Collection>): List<CollectionItem> {
        return data.flatMap { collection ->
                listOf(NameCollectionItem(collection.name)) +
                        collection.typedCards.flatMap { type ->
                            listOf(CardTypeItem(collection.code, type.type)) +
                                    type.cards.map { card -> CardItem(card) }
                        }
            }
    }
}