package br.com.bootcamp.magicgamecs.domain

import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.CardType
import br.com.bootcamp.magicgamecs.models.pojo.MagicSet
import br.com.bootcamp.magicgamecs.models.pojo.PageResult
import br.com.bootcamp.magicgamecs.models.repository.MagicRepository

class LoadMagicSetsByPage(
    private val repository: MagicRepository
) : UseCase<LoadMagicSetsByPage.Params, PageResult<List<MagicSet>>>() {

    private val sets = mutableListOf<MagicSet>()

    override suspend fun run(params: Params): PageResult<List<MagicSet>> {
        val magicSets = getSets()
        val magicSet = magicSets[params.page]

        if (magicSet.typedCards.isEmpty()) {
            var page = 0
            val limit = 100
            val allCards = mutableListOf<Card>()

            do {
                val result = repository.getCards(magicSet.code, ++page, limit)
                allCards.addAll(result)
            } while (result.size == limit)

            val grouped = allCards.groupBy { it.type }
                .map { CardType(it.key, it.value) }.slice(0..5)

            magicSet.typedCards.addAll(grouped)
        }
        val nextPage = (params.page + 1).takeIf { it < sets.size }
        return PageResult(listOf(magicSet), sets.size, nextPage)
    }

    private suspend fun getSets(): List<MagicSet> {
        if (sets.isEmpty()) {
            sets.addAll(repository.getSets())
        }
        return sets
    }

    data class Params(val page: Int = 0)
}