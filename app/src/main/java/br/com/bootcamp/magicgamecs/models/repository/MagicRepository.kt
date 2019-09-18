package br.com.bootcamp.magicgamecs.models.repository

import br.com.bootcamp.magicgamecs.models.Card
import br.com.bootcamp.magicgamecs.models.MagicSet

interface MagicRepository {
    suspend fun getSets(): List<MagicSet>

    suspend fun getCards(setCode: String, page: Int, limit: Int): List<Card>
}