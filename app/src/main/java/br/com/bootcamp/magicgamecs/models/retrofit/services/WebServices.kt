package br.com.bootcamp.magicgamecs.models.retrofit.services

import br.com.bootcamp.magicgamecs.models.pojo.Card
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {

    @GET("cards")
    suspend fun listCard(
        @Query("page") page: Int
    ): List<Card>
}