package br.com.bootcamp.magicgamecs.features.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.repository.impl.MagicRepositoryDb

class DetailViewModel(context: Context, val card: Card) : ViewModel() {

    private val repository = MagicRepositoryDb(context)
    private val allCards: LiveData<List<Card>> = repository.getAllFavoritesCards()
    private val favoriteCard: LiveData<Card> = repository.getCardFavorite(card)


    fun observerCards() = allCards

    fun getCardFavorite() = favoriteCard

    fun addCard() = repository.insert(card)

    fun removeCard() = repository.delete(card)
}