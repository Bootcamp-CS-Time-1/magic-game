package br.com.bootcamp.magicgamecs.models.repository.impl

import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.Collection
import br.com.bootcamp.magicgamecs.models.pojo.PageResult
import br.com.bootcamp.magicgamecs.models.repository.MagicRepository
import br.com.bootcamp.magicgamecs.models.retrofit.services.WebServices
import retrofit2.HttpException

class MagicRepositoryImpl(
    private val webServices: WebServices,
    private val pageLimit: Int = 100
) : MagicRepository {

    override suspend fun getAllSets(): List<Collection> =
        webServices.listSets()
            .let {
                if (it.isSuccessful) it.body()?.sets!!
                else throw HttpException(it)
            }
            .map { it.copy(typedCards = mutableListOf()) }

    override suspend fun getAllCardsBySetCode(setCode: String): List<Card> {
        val allCards = mutableListOf<Card>()

        var page = 0
        val orderBy = "releaseDate"

        do {
            val result = webServices.listCards(
                set = setCode,
                page = ++page,
                limit = pageLimit,
                orderBy = orderBy,
                term = null
            ).cards
            allCards.addAll(result)

            // enquanto o resultado for igual a quantidade solicitada, entao pode ser que tenha mais resultados
        } while (result.size == pageLimit)

        return allCards
    }

    override suspend fun searchCards(
        page: Int,
        lastSet: String?,
        term: String?
    ): PageResult<List<Card>> {
        var reqPage: Int? = page
        val orderBy = "set"

        val allCards = mutableListOf<Card>()
        var first: String? = null
        var nextPage: Int?

        do {
            val result = webServices.listCards(
                term = term,
                page = reqPage,
                limit = pageLimit,
                orderBy = orderBy,
                set = null
            ).cards

            val indexOfFirst = result
                .takeIf { lastSet != null && reqPage == page }
                ?.indexOfLast { it.set == lastSet }
                ?.takeIf { it > -1 }?.plus(1) ?: 0
            val clearResult = result.subList(indexOfFirst, result.size)
            first = first ?: clearResult.firstOrNull()?.set

            val cards = clearResult.takeWhile { it.set == first }
            allCards.addAll(cards)

            // e igual a ultima solicitada quando houver resultado diferente do `first`
            // e null quando result for diferente do limite
            nextPage = reqPage.takeIf { result.size == pageLimit }

            reqPage = reqPage.takeIf { result.isNotEmpty() }?.plus(1)

        } while ((cards.size + (result.size - clearResult.size)) == pageLimit)

        return PageResult(allCards, nextPage)
    }
}