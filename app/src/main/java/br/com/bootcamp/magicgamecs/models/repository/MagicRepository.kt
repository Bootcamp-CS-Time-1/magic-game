package br.com.bootcamp.magicgamecs.models.repository

import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.Collection

interface MagicRepository {
    suspend fun getSets(): List<Collection>

    suspend fun getAllCardsBySetCode(setCode: String): List<Card>
}