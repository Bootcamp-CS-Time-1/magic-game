package br.com.bootcamp.magicgamecs.models.repository.impl

import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.Collection
import br.com.bootcamp.magicgamecs.models.repository.MagicRepository
import br.com.bootcamp.magicgamecs.models.retrofit.services.WebServices
import retrofit2.HttpException

class MagicRepositoryImpl(
    private val webServices: WebServices
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
        val limit = 100
        val orderBy = "type"

        do {
            val result = webServices.listCard(setCode, ++page, limit, orderBy).cards
            allCards.addAll(result)

            // enquanto o resultado for igual a quantidade solicitada, entao pode ser que tenha mais resultados
        } while (result.size == limit)

        return allCards
    }
}