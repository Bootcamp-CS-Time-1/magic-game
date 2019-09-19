package br.com.bootcamp.magicgamecs.models.repository.impl

import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.Collection
import br.com.bootcamp.magicgamecs.models.repository.MagicRepository
import br.com.bootcamp.magicgamecs.models.retrofit.services.WebServices

class MagicRepositoryImpl(private val webServices: WebServices) : MagicRepository {
    override suspend fun getSets(): List<Collection> =
        webServices.listSets().sets
            .map { it.copy(typedCards = mutableListOf()) }

    override suspend fun getAllCardsBySetCode(setCode: String): List<Card> {
        val allCards = mutableListOf<Card>()
        val limit = 100
        var page = 0

        do {
            val result = webServices.listCard(setCode, ++page, limit).cards
            allCards.addAll(result)

            // enquanto o resultado for igual a quantidade solicitada, entao pode ser que tenha mais resultados
        } while (result.size == limit)

        return allCards
    }
}