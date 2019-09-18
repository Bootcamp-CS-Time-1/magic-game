package br.com.bootcamp.magicgamecs.models

data class MagicSet(
    val code: String,
    val name: String,
    val typedCards: MutableList<CardType> = mutableListOf()
)