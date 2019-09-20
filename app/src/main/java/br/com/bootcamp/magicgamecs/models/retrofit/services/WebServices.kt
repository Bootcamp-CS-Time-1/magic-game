package br.com.bootcamp.magicgamecs.models.retrofit.services

import br.com.bootcamp.magicgamecs.models.pojo.CardsResponse
import br.com.bootcamp.magicgamecs.models.pojo.CollectionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {

    @GET("sets")
    suspend fun listSets(): Response<CollectionResponse>

    @GET("cards")
    suspend fun listCard(
        @Query("set") set: String,
        @Query("page") page: Int,
        @Query("pageSize") limit: Int,
        @Query("orderBy") orderBy: String
    ): CardsResponse
}