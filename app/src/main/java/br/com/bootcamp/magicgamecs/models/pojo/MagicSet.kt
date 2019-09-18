package br.com.bootcamp.magicgamecs.models.pojo

import com.google.gson.annotations.Expose

data class MagicSet(
    val code: String,
    val name: String,
    @Expose(deserialize = false)
    val typedCards: MutableList<CardType> = mutableListOf()
)