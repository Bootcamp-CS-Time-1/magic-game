package br.com.bootcamp.magicgamecs.domain

import br.com.bootcamp.magicgamecs.models.Card
import br.com.bootcamp.magicgamecs.models.CardType
import br.com.bootcamp.magicgamecs.models.MagicSet
import br.com.bootcamp.magicgamecs.models.repository.MagicRepository

class LoadMagicSetsByPage(
    private val repository: MagicRepository
) : UseCase<LoadMagicSetsByPage.Params, MagicSet>() {

    private val sets = mutableListOf<MagicSet>()

    override suspend fun run(params: Params): MagicSet {
        val magicSet = getSets()[params.page]
        if (magicSet.typedCards.isEmpty()) {
            var page = 0
            val limit = 100
            val allCards = mutableListOf<Card>()

            do {
                page++
                val result = repository.getCards(magicSet.code, page, limit)
                allCards.addAll(result)
            } while (result.size == limit)

            val grouped = allCards.groupBy { it.type }
                .map { CardType(it.key, it.value) }

            magicSet.typedCards.addAll(grouped)
        }
        return magicSet
    }

    private suspend fun getSets(): List<MagicSet> {
        if (sets.isEmpty()) {
            sets.addAll(repository.getSets())
        }
        return sets
    }

    data class Params(val page: Int)
}