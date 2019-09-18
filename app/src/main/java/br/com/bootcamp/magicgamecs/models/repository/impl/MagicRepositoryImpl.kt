package br.com.bootcamp.magicgamecs.models.repository.impl

import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.MagicSet
import br.com.bootcamp.magicgamecs.models.repository.MagicRepository
import br.com.bootcamp.magicgamecs.models.retrofit.services.WebServices

class MagicRepositoryImpl(private val webServices: WebServices) : MagicRepository {
    override suspend fun getSets(): List<MagicSet> =
        webServices.listSets().sets.map {
            it.copy(typedCards = mutableListOf())
        }

    override suspend fun getCards(setCode: String, page: Int, limit: Int): List<Card> =
        webServices.listCard(setCode, page, limit).cards
}