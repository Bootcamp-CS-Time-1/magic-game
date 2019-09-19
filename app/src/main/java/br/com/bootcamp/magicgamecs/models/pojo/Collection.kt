package br.com.bootcamp.magicgamecs.models.pojo

import com.google.gson.annotations.Expose

data class Collection(
    val code: String,
    val name: String,
    @Expose(deserialize = false)
    val typedCards: MutableList<CardType> = mutableListOf()
)