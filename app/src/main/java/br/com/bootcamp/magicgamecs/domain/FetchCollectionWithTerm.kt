package br.com.bootcamp.magicgamecs.domain

import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.CardType
import br.com.bootcamp.magicgamecs.models.pojo.Collection
import br.com.bootcamp.magicgamecs.models.pojo.PageResult
import br.com.bootcamp.magicgamecs.models.repository.MagicRepository

class FetchCollectionWithTerm(
    private val repository: MagicRepository
) : UseCase<FetchCollectionWithTerm.Params, PageResult<List<Collection>>>() {

    private val allCards = mutableListOf<Card>()
    private var lastPage = 0

    override suspend fun run(params: Params): PageResult<List<Collection>> {
        require(params.page > lastPage) {
            "Requested page is invalid (${params.page})"
        }

        val cardsResult = repository.searchCards(params.page, allCards.lastOrNull()?.set)
        lastPage = params.page

        allCards.addAll(cardsResult.data)
        val collections = allCards
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

        return PageResult(
            // carrega os dados a partir da primeira pagina ate a solicitada
            data = collections,
            // passa nulo quando a proxima pagina nao existe
            nextPage = cardsResult.nextPage
        )
    }

    data class Params(val page: Int = 1, val term: String? = null)
}