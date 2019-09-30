package br.com.bootcamp.magicgamecs.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.CardType
import br.com.bootcamp.magicgamecs.models.pojo.Collection
import br.com.bootcamp.magicgamecs.models.repository.MagicRepository

class FetchFavoriteCards(
    private val repository: MagicRepository
) : UseCase.NoParam<LiveData<List<Collection>>>() {
    override fun run(): LiveData<List<Collection>> =
        Transformations.map(repository.getAllFavoritesCards()) { allCards ->
            allCards
                .fold(listOf<Card>()) { acc, card ->
                    acc + card.types.map { card.copy(type = it) }
                }
                .groupBy { Collection(it.set, it.setName) }
                .map { (collection, cards) ->
                    collection.copy(
                        typedCards = cards.groupBy { it.type }
                            .map { (type, cards) ->
                                CardType(type, cards.sortedBy { it.name })
                            }
                            .sortedBy { it.type }
                            .toMutableList()
                    )
                }
        }
}