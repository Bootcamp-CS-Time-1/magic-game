package br.com.bootcamp.magicgamecs.models.repository

import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.MagicSet

interface MagicRepository {
    suspend fun getSets(): List<MagicSet>

    suspend fun getCards(setCode: String, page: Int, limit: Int): List<Card>
}