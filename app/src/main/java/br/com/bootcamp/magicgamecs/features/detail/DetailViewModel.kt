package br.com.bootcamp.magicgamecs.features.detail

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.repository.MagicRepository
import br.com.bootcamp.magicgamecs.models.repository.impl.MagicRepositoryImpl
import kotlinx.coroutines.launch

class DetailViewModel(val repository: MagicRepository) : ViewModel() {


    private val allCards: LiveData<List<Card>> = repository.getAllFavoritesCards()


    fun observerCards() = allCards

    fun getCardFavorite(card: Card) = repository.getCardFavorite(card)

    fun addCard(card: Card) {
        viewModelScope.launch {
            repository.insertCardToFavorite(card)
        }
    }

    fun removeCard(card: Card) {
        viewModelScope.launch {
            repository.deleteCardToFavorite(card)
        }
    }
}