package br.com.bootcamp.magicgamecs.models.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.bootcamp.magicgamecs.models.pojo.Card

@Dao
interface CardDao {

    @Query("SELECT * FROM card")
    fun loadAllCards(): LiveData<List<Card>>

    @Query("SELECT * FROM card WHERE id = :id")
    fun searchFavoriteCard(id: String) : LiveData<Card>

    @Insert
    fun insertCard(card: Card)

    @Delete
    fun deleteCard(card: Card)
}