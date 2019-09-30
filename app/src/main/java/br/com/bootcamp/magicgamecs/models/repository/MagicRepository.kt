package br.com.bootcamp.magicgamecs.models.repository

import androidx.lifecycle.LiveData
import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.Collection

interface MagicRepository {
    suspend fun getAllSets(): List<Collection>

    suspend fun getAllCardsBySetCode(setCode: String): List<Card>

    suspend fun insertCardToFavorite(card: Card)

    suspend fun deleteCardToFavorite(card: Card)

    fun getAllFavoritesCards(): LiveData<List<Card>>

    fun getCardFavorite(card: Card): LiveData<Card>

}