package br.com.bootcamp.magicgamecs.models.retrofit.services

import br.com.bootcamp.magicgamecs.models.pojo.CardsResponse
import br.com.bootcamp.magicgamecs.models.pojo.MagicSetsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {

    @GET("sets")
    suspend fun listSets(): MagicSetsResponse

    @GET("cards")
    suspend fun listCard(
        @Query("set") set: String,
        @Query("page") page: Int,
        @Query("pageSize") limit: Int
    ): CardsResponse
}