package br.com.bootcamp.magicgamecs.models.repository.impl

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.room.AppDataBase
import br.com.bootcamp.magicgamecs.models.room.CardDao

class MagicRepositoryDb(private val cardDao: CardDao) {

//    fun getAllFavoritesCards() = cardDao.loadAllCards()
//
//    fun getCardFavorite(card: Card): LiveData<Card> {
//        return cardDao.searchFavoriteCard(card.id)
//    }
//
//    suspend fun insertCardToFavorite(card: Card) {
//        cardDao.insertCard(card)
//    }
//
//    suspend fun deleteCardToFavorite(card: Card) {
//        cardDao.deleteCard(card)
//    }


}