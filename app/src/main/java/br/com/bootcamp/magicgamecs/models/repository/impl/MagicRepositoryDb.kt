package br.com.bootcamp.magicgamecs.models.repository.impl

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.room.AppDataBase
import br.com.bootcamp.magicgamecs.models.room.CardDao

class MagicRepositoryDb(context: Context) {

    private var cardDao: CardDao
    private var allCards: LiveData<List<Card>>

    init {
        val dataBase = AppDataBase.getInstance(context)!!
        cardDao = dataBase.cardDao()
        allCards = cardDao.loadAllCards()
    }


    fun getAllFavoritesCards(): LiveData<List<Card>> {
        return allCards
    }

    fun getCardFavorite(card: Card): LiveData<Card> {
        return cardDao.searchFavoriteCard(card.id)
    }

    fun insert(card: Card) {
        val insertCardAsyncTask = InsertCardAsyncTask(cardDao).execute(card)
    }

    fun delete(card: Card) {
        val deleteCardAsyncTask = DeleteCardAsyncTask(cardDao).execute(card)
    }


    private class InsertCardAsyncTask(cardDao: CardDao) : AsyncTask<Card, Unit, Unit>() {

        val cardDao = cardDao

        override fun doInBackground(vararg p0: Card?) {
            p0[0]?.let { cardDao.insertCard(it) }
        }

    }

    private class DeleteCardAsyncTask(cardDao: CardDao) : AsyncTask<Card, Unit, Unit>() {

        val cardDao = cardDao

        override fun doInBackground(vararg p0: Card?) {
            p0[0]?.let { cardDao.deleteCard(it) }
        }

    }

}