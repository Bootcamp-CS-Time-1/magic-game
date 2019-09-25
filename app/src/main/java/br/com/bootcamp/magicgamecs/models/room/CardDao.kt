package br.com.bootcamp.magicgamecs.models.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.bootcamp.magicgamecs.models.pojo.Card

@Dao
interface CardDao {

    @Query("SELECT * FROM card")
    fun loadAllCards() : List<Card>

    @Insert
    fun insertCard(card: Card)
}