package br.com.bootcamp.magicgamecs.models.repository

import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.Collection
import br.com.bootcamp.magicgamecs.models.pojo.PageResult

interface MagicRepository {
    suspend fun getAllSets(): List<Collection>

    suspend fun getAllCardsBySetCode(setCode: String): List<Card>

    suspend fun searchCards(
        page: Int,
        lastSet: String? = null,
        term: String? = null
    ): PageResult<List<Card>>
}